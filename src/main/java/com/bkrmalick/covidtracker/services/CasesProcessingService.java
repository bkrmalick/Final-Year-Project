package com.bkrmalick.covidtracker.services;

import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInputRow;
import com.bkrmalick.covidtracker.models.cases_api.output.CasesApiOutputRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInput;
import com.bkrmalick.covidtracker.models.cases_api.output.CasesApiOutput;

import java.util.*;

@Service
public class CasesProcessingService
{
	private final static String [] BOROUGHS={"Barking and Dagenham","Barnet","Bexley","Brent","Bromley","Camden","Croydon","Ealing","Enfield","Greenwich","Hackney and City of London","Hammersmith and Fulham","Haringey","Harrow","Havering","Hillingdon","Hounslow","Islington","Kensington and Chelsea","Kingston upon Thames","Lambeth","Lewisham","Merton","Newham","Redbridge","Richmond upon Thames","Southwark","Sutton","Tower Hamlets","Waltham Forest","Wandsworth","Westminster"};
	private CasesDataAccessService casesDataAccessService;

	@Autowired
	public CasesProcessingService(CasesDataAccessService casesDataAccessService)
	{
		this.casesDataAccessService=casesDataAccessService;
	}

	public CasesApiOutput produceOutputResponse()
	{
		/*GET THE INPUT DATA FROM EXT API*/
		Date lastRefreshDate= casesDataAccessService.getLastRefreshDate();
		CasesApiInput dataForTwoWeeks = casesDataAccessService.getDataForTwoWeeksBeforeDate(lastRefreshDate);

		/*PROCESS DATA*/
		CasesApiOutput responseToSend=processCasesApiResponse(dataForTwoWeeks, lastRefreshDate);

		return responseToSend;
	}

	public CasesApiOutput processCasesApiResponse(CasesApiInput input, Date lastRefreshDate)
	{
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

		double dangerPercentage=calculateDangerPercentage(inputRows, borough,totalCases);

		return new CasesApiOutputRow(borough,dangerPercentage,totalCases,casesInPastTwoWeeks);
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


}
