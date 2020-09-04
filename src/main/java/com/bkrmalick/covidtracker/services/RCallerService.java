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
import java.util.stream.Collectors;

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

		this.scriptContent=Files.lines(inputScript).collect(Collectors.joining("\n"));
	}

	public double mean(int[] values) throws IOException, URISyntaxException, ScriptException
	{
		RenjinScriptEngine engine = new RenjinScriptEngine();

		//only read trigger read script if hasn't been loaded already
		if(this.scriptContent==null)
		{
			loadScriptContentFromFile();
		}

		String meanScriptContent = this.scriptContent;
		engine.put("input", values);
		engine.eval(meanScriptContent);
		DoubleArrayVector result = (DoubleArrayVector) engine.eval("customMean(input)");
		return result.asReal();
	}

	public int[] getPredictedCasesUntilDate(LocalDate date, String borough) throws IOException, URISyntaxException, ScriptException
	{
		RenjinScriptEngine engine = new RenjinScriptEngine();
		String boroughData= getBoroughDataAsDataFrame(borough);

		//only read script if hasn't been loaded already
		if(this.scriptContent==null)
		{
			loadScriptContentFromFile();
		}

		engine.put("predict_date", date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		engine.put("bdata", boroughData);

		engine.eval(this.scriptContent);

		IntArrayVector result = (IntArrayVector) engine.eval("predictCasesUntilDate(predict_date,bdata)");
		return result.toIntArray();
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
