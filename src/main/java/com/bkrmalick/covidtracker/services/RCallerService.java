package com.bkrmalick.covidtracker.services;

import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInput;
import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInputRow;
import org.renjin.script.RenjinScriptEngine;
import org.renjin.sexp.*;
import org.renjin.sexp.ListVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RCallerService
{
	private CasesDataAccessService casesDataAccessService;

	private String[] BOROUGHS;

	@Autowired
	private RCallerService proxy; //cached methods MUST only be called through this proxy

	//private HashMap<String, CasesApiInputRow[]> predictionCache; //<Borough, predicted rows>
	//private LocalDate lastRefreshedDateForCache; //lastRefresheddate on data for which cache was generated

	@Autowired
	public RCallerService(CasesDataAccessService casesDataAccessService, @Qualifier("BOROUGH_NAMES") String[] BOROUGHS)
	{
		this.casesDataAccessService = casesDataAccessService;
		this.BOROUGHS = BOROUGHS;
		//this.predictionCache = new HashMap<>();
		//this.lastRefreshedDateForCache = null;
	}

	@Cacheable(value="scripts")
	public String getScriptContent() throws IOException, URISyntaxException
	{
		String scriptContent;

		System.out.println("READ FILE");
		URI rScriptUri = RCallerService.class.getClassLoader().getResource("r-scripts/cases-predictor.R").toURI();
		Path inputScript = Paths.get(rScriptUri);

		//try block will automatically close file
		try(Stream<String> lines = Files.lines(inputScript))
		{
			scriptContent = lines.collect(Collectors.joining("\n"));
		}

		return scriptContent;
	}

	/**
	 * Runs the R script to train model on all data for borough,
	 * and then returns predicted data leading up to date provided for that specific borough only.
	 * @param date for which to predict up until
	 * @param borough
	 * @return predicted cases data rows
	 */

	@Cacheable(value="predictions", key="") //#date.isAfter()
	public CasesApiInputRow[] getPredictedDataUntilDate(LocalDate date, String borough) throws IOException, ScriptException, URISyntaxException
	{
		System.out.println("PREDICTION");
		RenjinScriptEngine engine = new RenjinScriptEngine();
		String boroughData= getBoroughDataAsDataFrame(borough);

		engine.put("predict_date", date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;

		engine.eval(proxy.getScriptContent());

		ListVector predictionsDataFrame = (ListVector) engine.eval("predictCasesUntilDate(predict_date,"+boroughData+")");

		Vector predictionDates=(Vector) predictionsDataFrame.get("date");
		Vector predictionTotalCases=(Vector) predictionsDataFrame.get("total_cases");
		Vector predictionNewCases=(Vector) predictionsDataFrame.get("new_cases");

		int numberOfPredictions= predictionDates.length();

		CasesApiInputRow [] predictionRows = new CasesApiInputRow[numberOfPredictions];

		for(int i=0;i<numberOfPredictions;i++)
		{
			predictionRows[i]= new CasesApiInputRow(
					borough,
					null,
					LocalDate.ofEpochDay(predictionDates.getElementAsInt(i)),
					predictionNewCases.getElementAsInt(i),
					predictionTotalCases.getElementAsInt(i));
		}

		return predictionRows;
	}

	public CasesApiInput getPredictedDataForDate(LocalDate date) throws IOException, URISyntaxException, ScriptException
	{
		ArrayList<CasesApiInputRow> rowsList= new ArrayList<>();

		//loop throw the boroughs and call getPredictedDataUntilDate
		for(String borough:BOROUGHS)
		{
			rowsList.addAll(
					Arrays.asList(proxy.getPredictedDataUntilDate(date,borough))
			);
		}

		return new CasesApiInput(null, null,  rowsList.toArray(new CasesApiInputRow[0]));
	}

	private String getBoroughDataAsDataFrame(String borough)
	{
		CasesApiInputRow [] casesData=casesDataAccessService.getAllDataForBorough(borough).getRows();

		String dataFrame="data.frame(date=as.Date(c(";

		StringBuilder datesCSV= new StringBuilder(); //e.g "2020-03-03","2020-03-04"
		StringBuilder totalCasesCSV= new StringBuilder(); //e.g 1024,1026

		for(CasesApiInputRow row:casesData)
		{
			datesCSV.append(", \"" +row.getDate()+"\"");
			totalCasesCSV.append(","+row.getTotal_cases());
		}

		datesCSV.setCharAt(0,' ');
		totalCasesCSV.setCharAt(0,' ');

		dataFrame+=datesCSV+")), total_cases=c("+totalCasesCSV+"))";

		return dataFrame;
	}
}
