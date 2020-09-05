package com.bkrmalick.covidtracker.services;

import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInput;
import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInputRow;
import org.renjin.script.RenjinScriptEngine;
import org.renjin.sexp.*;
import org.renjin.sexp.ListVector;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RCallerService
{
	@Autowired
	private CasesDataAccessService casesDataAccessService;
	private String scriptContent;

	/**
	 * Will only get called once during applications run
	 */
	void loadScriptContentFromFile() throws IOException, URISyntaxException
	{
		URI rScriptUri = RCallerService.class.getClassLoader().getResource("r-scripts/cases-predictor.R").toURI();
		Path inputScript = Paths.get(rScriptUri);

		//try block will automatically close file
		try(Stream<String> lines = Files.lines(inputScript))
		{
			this.scriptContent = lines.collect(Collectors.joining("\n"));
		}
	}

	/**
	 * Runs the R script to train model on all data for borough,
	 * and then returns predicted data for two weeks leading up to date provided.
	 * @param date for which to predict up until
	 * @param borough
	 * @return predicted cases data rows
	 */
	public CasesApiInputRow[] getPredictedDataUntilDate(LocalDate date, String borough) throws IOException, ScriptException, URISyntaxException
	{
		RenjinScriptEngine engine = new RenjinScriptEngine();
		String boroughData= getBoroughDataAsDataFrame(borough);

		//only read script if hasn't been loaded already
		if(this.scriptContent==null)
		{
			loadScriptContentFromFile();
		}

		engine.put("predict_date", date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;

		engine.eval(this.scriptContent);

		ListVector predictionsDataFrame = (ListVector) engine.eval("predictCasesUntilDate(predict_date,"+boroughData+")");

		Vector predictionDates=(Vector) predictionsDataFrame.get("date");
		Vector predictionTotalCases=(Vector) predictionsDataFrame.get("total_cases");

		int numberOfPredictions= predictionDates.length();

		CasesApiInputRow [] predictionRows = new CasesApiInputRow[numberOfPredictions];

		for(int i=0;i<numberOfPredictions;i++)
		{
			predictionRows[i]= new CasesApiInputRow(
					borough,
					null,
					LocalDate.ofEpochDay(predictionDates.getElementAsInt(i)),
					0, //todo
					predictionTotalCases.getElementAsInt(i));
		}

		return predictionRows;
	}

	public CasesApiInput getPredictedDataForDate(LocalDate date) throws IOException, URISyntaxException, ScriptException
	{
		ArrayList<CasesApiInputRow> rowsList= new ArrayList<>();

		//loop throw the boroughs and call getPredictedDataUntilDate

		return new CasesApiInput(null, null,  (CasesApiInputRow[]) rowsList.toArray());
	}

	private String getBoroughDataAsDataFrame(String borough)
	{
		CasesApiInputRow [] casesData=casesDataAccessService.getAllDataForBorough(borough).getRows();

		String dataFrame="data.frame(date=c(";

		StringBuilder datesCSV= new StringBuilder(); //e.g "2020-03-03","2020-03-04"
		StringBuilder totalCasesCSV= new StringBuilder(); //e.g 1024,1026

		for(CasesApiInputRow row:casesData)
		{
			datesCSV.append(", \"" +row.getDate()+"\"");
			totalCasesCSV.append(","+row.getTotal_cases());
		}

		datesCSV.setCharAt(0,' ');
		totalCasesCSV.setCharAt(0,' ');

		dataFrame+=datesCSV+"), total_cases=c("+totalCasesCSV+"))";

		return dataFrame;
	}
}
