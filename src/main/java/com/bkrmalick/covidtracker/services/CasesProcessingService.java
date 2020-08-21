package com.bkrmalick.covidtracker.services;

import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInputRow;
import com.bkrmalick.covidtracker.models.cases_api.output.CasesApiOutputRow;
import com.bkrmalick.covidtracker.models.dynamo_db.PopulationDensityRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInput;
import com.bkrmalick.covidtracker.models.cases_api.output.CasesApiOutput;

import java.util.*;

@Service
public class CasesProcessingService
{
	private final String [] BOROUGHS;
	private CasesDataAccessService casesDataAccessService;
	private PopulationDensityDataAccessService populationDensityDataAccessService;

	@Autowired
	public CasesProcessingService(CasesDataAccessService casesDataAccessService, @Qualifier("BOROUGH_NAMES") String [] BOROUGHS, PopulationDensityDataAccessService populationDensityDataAccessService)
	{
		this.casesDataAccessService=casesDataAccessService;
		this.BOROUGHS=BOROUGHS;
		this.populationDensityDataAccessService = populationDensityDataAccessService;
	}

	public CasesApiOutput produceOutputResponse()
	{
		/*GET THE INPUT DATA FROM EXT API*/
		Date lastRefreshDate= casesDataAccessService.getLastRefreshDate();
		CasesApiInput dataForTwoWeeks = casesDataAccessService.getDataForDaysBeforeDate(lastRefreshDate,14);

		/*PROCESS DATA*/
		CasesApiOutput responseToSend=processCasesApiResponse(dataForTwoWeeks, lastRefreshDate);

		return responseToSend;
	}

	public CasesApiOutput processCasesApiResponse(CasesApiInput input, Date lastRefreshDate)
	{
		populationDensityDataAccessService.getPopulationDensityRecordForBoroughForCurrentYear("Bexley");
		CasesApiInputRow[] inputRows= input.getRows();
		CasesApiOutputRow[] outputRows = new CasesApiOutputRow[BOROUGHS.length];

		for(int i=0;i<BOROUGHS.length;i++)
		{
			outputRows[i]=produceOutputRow(inputRows, BOROUGHS[i]);
		}

		CasesApiOutput output=new CasesApiOutput(outputRows, lastRefreshDate);

		return output;
	}

	private CasesApiOutputRow produceOutputRow(CasesApiInputRow[] inputRows, String borough)
	{
		int totalCases=Arrays.stream(inputRows)
				.filter(row->row.getArea_name().equals(borough))
				.sorted(Comparator.comparing(CasesApiInputRow::getDate).reversed())
				.findFirst()
				.orElseThrow(()->new IllegalStateException("Cannot find latest input row for borough ["+borough+"]"))
				.getTotal_cases();

		int casesInPastTwoWeeks=Arrays.stream(inputRows)
				.filter(row->row.getArea_name().equals(borough))
				.mapToInt(row-> row.getNew_cases())
				.sum();

		double populationDensityPerSqKM=getPopulationDensityForBorough(borough);

		double dangerPercentage=//calculateDangerPercentage(inputRows, borough,totalCases);
				calculateAbsoluteDangerValue(casesInPastTwoWeeks,populationDensityPerSqKM);//todo scale this value and check if long needed

		return new CasesApiOutputRow(borough,dangerPercentage,totalCases,casesInPastTwoWeeks, populationDensityPerSqKM);
	}

	/**
	 * Calculates the danger level as totalcases/highesttotalcases
	 * todo find out relation using r?
	 */
	private double calculateDangerPercentage(CasesApiInputRow[] inputRows,String borough, int totalCases)
	{
		int maxTotalCases=Arrays.stream(inputRows)
				.max(Comparator.comparing(CasesApiInputRow::getTotal_cases))
				.orElseThrow(()->new IllegalStateException("Cannot find max total cases of boroughs"))
				.getTotal_cases();

		double rValue=(double)totalCases*100/maxTotalCases;

		rValue =Math.round(rValue *100.0)/100.0;
		return  rValue;
	}

	/**
	 *  danger percentage = cases in past two weeks * population density
	 */
	private double calculateAbsoluteDangerValue(int casesPastTwoWeeks,double populationDensity)
	{
		return (double) casesPastTwoWeeks * populationDensity;
	}

	private double roundToTwoDecimalPlaces(double value)
	{
		return Math.round(value * 100.0) / 100.0;
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
