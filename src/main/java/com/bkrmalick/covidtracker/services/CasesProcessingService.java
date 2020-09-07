package com.bkrmalick.covidtracker.services;

import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInputRow;
import com.bkrmalick.covidtracker.models.cases_api.output.CasesApiOutputRow;
import com.bkrmalick.covidtracker.models.dynamo_db.PopulationDensityRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInput;
import com.bkrmalick.covidtracker.models.cases_api.output.CasesApiOutput;

import javax.script.ScriptException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;

@Service
public class CasesProcessingService
{
	private final String [] BOROUGHS;
	private CasesDataAccessService casesDataAccessService;
	private PopulationDensityDataAccessService populationDensityDataAccessService;
	private RCallerService rCallerService;


	@Autowired
	public CasesProcessingService(CasesDataAccessService casesDataAccessService, @Qualifier("BOROUGH_NAMES") String[] BOROUGHS, PopulationDensityDataAccessService populationDensityDataAccessService, RCallerService rCallerService)
	{
		this.casesDataAccessService=casesDataAccessService;
		this.BOROUGHS=BOROUGHS;
		this.populationDensityDataAccessService = populationDensityDataAccessService;
		this.rCallerService = rCallerService;
	}

	/**
	 * Called by the controller
	 * @param LocalDate The user defined date for which the response/output is to be produced. Can be in the future or past.
	 * @return CasesApiOutput The final response to be shown to the user
	 */
	public CasesApiOutput produceOutputResponse(LocalDate date)
	{
		/*GET THE INPUT DATA FROM EXT API*/
		LocalDate dataLastRefreshedDate= casesDataAccessService.getDataLastRefreshedDate();

		CasesApiInput inputData = null;
		CasesApiOutput outputData;

		LocalDate dataForDate;

		if(date!=null && date.isAfter(dataLastRefreshedDate))
		{
			/*PREDICTION MODE - user asking for data beyond the data available*/
			dataForDate=date;

			try
			{
				//System.out.println(rCallerService.getPredictedDataUntilDate(date, "Bexley"));
				inputData=rCallerService.getPredictedDataForDate(date);
			}
			catch(IOException | ScriptException | URISyntaxException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			/*NORMAL MODE - user asking data for historical data*/
			dataForDate = (date == null ? dataLastRefreshedDate : date);

			inputData= casesDataAccessService.getDataForDaysBeforeDate(dataForDate,14);
		}

		outputData = processCasesApiResponse(
				inputData,
				dataLastRefreshedDate,
				dataForDate );

		return outputData;
	}

	public CasesApiOutput processCasesApiResponse(CasesApiInput input, LocalDate dataLastRefreshedDate, LocalDate date)
	{
		CasesApiInputRow[] inputRows= input.getRows();
		CasesApiOutputRow[] outputRows = new CasesApiOutputRow[BOROUGHS.length];

		//get the output rows
		for(int i=0;i<BOROUGHS.length;i++)
		{
			outputRows[i]=produceOutputRow(inputRows, BOROUGHS[i]);
		}

		//populate relative danger percentages
		for(int i=0;i<BOROUGHS.length;i++)
		{
			outputRows[i].setDanger_percentage(
					calculateDangerPercentage(outputRows,outputRows[i].getDanger_value())
			);
		}

		return new CasesApiOutput(outputRows, dataLastRefreshedDate, date);
	}

	private CasesApiOutputRow produceOutputRow(CasesApiInputRow[] inputRows, String borough)
	{
		int totalCases= Arrays.stream(inputRows)
				.filter(row -> row.getArea_name().equals(borough))
				.max(Comparator.comparing(CasesApiInputRow::getDate))
				.orElseThrow(()->new IllegalStateException("Cannot find latest cases input row for borough ["+borough+"]"))
				.getTotal_cases();

		int casesInPastTwoWeeks=Arrays.stream(inputRows)
				.filter(row->row.getArea_name().equals(borough))
				.mapToInt(CasesApiInputRow::getNew_cases)
				.sum();

		double populationDensityPerSqKM=getPopulationDensityForBorough(borough);

		double dangerPercentage=0;

		BigDecimal dangerValue=calculateAbsoluteDangerValue(casesInPastTwoWeeks,populationDensityPerSqKM);

		return new CasesApiOutputRow(borough, dangerValue, dangerPercentage,totalCases,casesInPastTwoWeeks, populationDensityPerSqKM);
	}

	/**
	 * danger percentage =(dangerValueOfBorough/maxDangerValue * 100 )
	 */
	private double calculateDangerPercentage(CasesApiOutputRow[] outputRows, BigDecimal dangerValueOfBorough)
	{
		BigDecimal maxDanger_value=Arrays.stream(outputRows)
				.max(Comparator.comparing(CasesApiOutputRow::getDanger_value))
				.orElseThrow(()->new IllegalStateException("Cannot find max danger value of boroughs"))
				.getDanger_value();

		return dangerValueOfBorough
				.divide(maxDanger_value, 4, RoundingMode.HALF_EVEN)
				.multiply(BigDecimal.valueOf(100))
				.doubleValue();
	}

	/**
	 *  absolute danger value  = cases in past two weeks * population density
	 */
	private BigDecimal calculateAbsoluteDangerValue(int casesPastTwoWeeks,double populationDensity)
	{
		BigDecimal bd_cases = BigDecimal.valueOf(casesPastTwoWeeks);
		BigDecimal bd_populationDensity = BigDecimal.valueOf(populationDensity);

		return bd_cases
				.multiply(bd_populationDensity);
	}

	private double getPopulationDensityForBorough(String borough)
	{
		double boroughPopDensity;

		if(borough.equalsIgnoreCase("Hackney and City of London"))
		{
			PopulationDensityRecord popDensityRecordHackney = populationDensityDataAccessService
					.getPopulationDensityRecordForBoroughForCurrentYear("Hackney");

			PopulationDensityRecord popDensityRecordCOL = populationDensityDataAccessService
					.getPopulationDensityRecordForBoroughForCurrentYear("City of London");

			double areaHackney=popDensityRecordHackney.getSquareKilometres();
			double areaCOL=popDensityRecordCOL.getSquareKilometres();
			double combinedArea=areaHackney+areaCOL;

			double popDensityHackney=popDensityRecordHackney.getPopulationPerSquareKilometre();
			double popDensityCOL=popDensityRecordCOL.getPopulationPerSquareKilometre();

			//combine the population densities acc. to area sizes
			boroughPopDensity=(popDensityHackney*areaHackney/combinedArea) + (popDensityCOL*areaCOL/combinedArea);
		}
		else
		{
			boroughPopDensity = populationDensityDataAccessService
					.getPopulationDensityRecordForBoroughForCurrentYear(borough)
					.getPopulationPerSquareKilometre();
		}

		return boroughPopDensity;
	}
}
