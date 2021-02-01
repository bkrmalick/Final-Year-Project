package com.bkrmalick.covidtracker.services;


import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInput;
import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInputRow;
import org.apache.commons.io.IOUtils;
import org.renjin.script.RenjinScriptEngine;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

/**
 * @Cacheable methods MUST
 * 			+ be public
 * 			+ only be called through the proxy
 */
@Service
//@Scope("prototype")
public class RCallerService
{
	private static final Logger logger = LoggerFactory.getLogger(RCallerService.class);

	private final CasesDataAccessService casesDataAccessService;
	private final String[] BOROUGHS;

	private RCallerService proxy; //@Cacheable methods MUST only be called through this proxy

	public static LocalDate maxDateCache; //request Date on data for which cache was generated
	public static LocalDate lastRefreshedDateCache; //lastRefreshedDate on data for which cache was generated

	@Autowired
	public RCallerService(CasesDataAccessService casesDataAccessService,
						  @Qualifier("BOROUGH_NAMES") String[] BOROUGHS)
	{
		this.casesDataAccessService = casesDataAccessService;
		this.BOROUGHS = BOROUGHS;
	}

	@Autowired
	public void setProxy(RCallerService proxy)
	{
		this.proxy = proxy;
	}

	@Cacheable(value="scripts")
	public String getScriptContent() throws IOException, URISyntaxException
	{
		String scriptContent;

		logger.info("READING R SCRIPT FILE");

		//try-with-resources block for closing stream
		try(InputStream in = getClass().getResourceAsStream("/r-scripts/cases-predictor.R"))
		{
			scriptContent = IOUtils.toString(in, StandardCharsets.UTF_8);
		}

		return scriptContent;
	}

	/**
	 * Runs the R script to train model on all data for borough,
	 * and then returns combined original + predicted data leading up to date
	 * provided for that specific borough only.
	 *
	 * @param date for which to predict up until
	 * @param borough
	 * @return predicted cases data rows
	 */
	@Cacheable(value="predictedCases",key="#borough", sync = true)
	public CasesApiInputRow[] getPredictedDataUntilDate(LocalDate date, String borough) throws IOException, ScriptException, URISyntaxException
	{
		logger.info("TRAINING MODEL AND PREDICTING DATA FOR ["+borough+"] ON ["+date+"]" );

		RenjinScriptEngine engine = new RenjinScriptEngine();
		String boroughData= getBoroughDataAsDataFrame(borough);

		engine.put("predict_date", date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		//logger.info("borough "+borough+" DATA : "+boroughData);
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

	/**
	 * Checks if the data for the new date being queried should replace old cache or not
	 */
	public boolean isCacheable(LocalDate requestDate, LocalDate lastRefreshedDate)
	{
		return RCallerService.maxDateCache ==null ||  //no caches previously
				!lastRefreshedDate.isEqual(RCallerService.lastRefreshedDateCache) || //external data has since been refreshed
				requestDate.isAfter(RCallerService.maxDateCache); //requestDate > cacheDate
	}

//	public CasesApiInput getPredictedCasesDataForDate(LocalDate date, int days, LocalDate lastRefreshedDate) throws IOException, URISyntaxException, ScriptException, InterruptedException
//	{
//		//semaphore.acquire();
//		CasesApiInput toReturn = getPredictedCasesDataForDateHelper(date,days,lastRefreshedDate);
//		//semaphore.release();
//
//		return toReturn;
//	}


	/**
	 *  NOT THREAD SAFE, CALL FROM OTHER METHOD
	 * @param date
	 * @param days
	 * @param lastRefreshedDate
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ScriptException
	 * @throws InterruptedException
	 */
	@CacheEvict(value="predictedCases", condition ="#root.target.isCacheable(#date,#lastRefreshedDate)", allEntries = true, beforeInvocation = true)
	public CasesApiInput getPredictedCasesDataForDate(LocalDate date, int days, LocalDate lastRefreshedDate) throws IOException, URISyntaxException, ScriptException, InterruptedException
	{
		logger.info("INCOMING PREDICTION DATA REQUEST FOR ["+date+"]");

		if(isCacheable(date,lastRefreshedDate))
		{
			//set these cache variables as we can be sure that @CacheEvict was triggered
			//and that the next run of getPredictedDataUntilDate
			//will store new cache

			RCallerService.maxDateCache = date;
			RCallerService.lastRefreshedDateCache=lastRefreshedDate;
			logger.info("ANY EXISTING CACHE REMOVED, CACHE TO BE UPDATED");
		}
		else
		{
			logger.info("RETURNING DATA FROM CACHE");
		}

		ArrayList<CasesApiInputRow> rowsList= new ArrayList<>();

		//loop throw the boroughs and call getPredictedDataUntilDate
		for(String borough:BOROUGHS)
		{
			//filter to data days leading up to  date
			List<CasesApiInputRow> dataForBorough=
					Arrays.stream(proxy.getPredictedDataUntilDate(date, borough))
					.filter(r-> r.getDate().isAfter(date.minusDays(days))  && r.getDate().isBefore(date.plusDays(1)))
					.collect(Collectors.toList());

			Assert.isTrue(dataForBorough.size()==14,"Borough ["+borough+"] has data for only ["+dataForBorough.size()+"] days" ); //todo remove
			//todo throwing  exception if multiple concurrent calls or cancelled requests from front end

			rowsList.addAll(dataForBorough);
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
