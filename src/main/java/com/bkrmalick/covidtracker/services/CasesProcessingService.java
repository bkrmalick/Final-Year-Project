package com.bkrmalick.covidtracker.services;

import com.bkrmalick.covidtracker.exceptions.GeneralUserVisibleException;
import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInput;
import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInputRow;
import com.bkrmalick.covidtracker.models.cases_api.output.CasesApiOutput;
import com.bkrmalick.covidtracker.models.cases_api.output.CasesApiOutputRow;
import com.bkrmalick.covidtracker.models.dynamo_db.PopulationDensityRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;

@Service
public class CasesProcessingService
{
	private static final Logger logger = LoggerFactory.getLogger(CasesProcessingService.class);

	private final String[] BOROUGHS;
	private final CasesDataAccessService casesDataAccessService;
	private final PopulationDensityDataAccessService populationDensityDataAccessService;
	private final RPredictorService rPredictorService;

//	public static Semaphore semaphore; //TODO change to private?

	@Autowired
	public CasesProcessingService(CasesDataAccessService casesDataAccessService,
								  @Qualifier("BOROUGH_NAMES") String[] BOROUGHS,
								  PopulationDensityDataAccessService populationDensityDataAccessService,
								  RPredictorService rPredictorService)
	{
		this.casesDataAccessService = casesDataAccessService;
		this.BOROUGHS = BOROUGHS;
		this.populationDensityDataAccessService = populationDensityDataAccessService;
		this.rPredictorService = rPredictorService;
		//this.semaphore = semaphore;
	}

	/**
	 * Called by the controller
	 *
	 * @param date The user defined date for which the response/output is to be produced. Can be in the future or past.
	 * @return CasesApiOutput The final response to be shown to the user
	 */
	public CasesApiOutput produceOutputResponse(LocalDate date)
	{

		LocalDate dataLastRefreshedDate = casesDataAccessService.getDataLastRefreshedDate();

		CasesApiInput inputData = null;
		CasesApiOutput outputData;

		LocalDate dateForOutput;

		if (date != null && date.isAfter(dataLastRefreshedDate))
		{
			/*PREDICTION MODE - user asking for data beyond the data available*/
			dateForOutput = date;

			try
			{
				inputData = rPredictorService.getPredictedCasesDataForDate(date, 14, dataLastRefreshedDate);
			}
			catch(InterruptedException e)
			{
				logger.error(String.format("Thread %s was interrupted", Thread.currentThread().getName()),e);
				Thread.currentThread().interrupt(); //https://stackoverflow.com/a/20934895
				throw new GeneralUserVisibleException("There was an error processing your request, please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			catch (IOException | ScriptException | URISyntaxException e)
			{
				logger.error("Exception while trying to predict",e);
				throw new GeneralUserVisibleException("There was an error while trying to predict. Please contact admin.", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else
		{
			/*NORMAL MODE - user asking data for historical data*/
			dateForOutput = (date == null ? dataLastRefreshedDate : date);

			//get the data from ext api
			inputData = casesDataAccessService.getDataForDaysBeforeDate(dateForOutput, 14);
		}

		outputData = processCasesApiResponse(
				inputData,
				dataLastRefreshedDate,
				dateForOutput);

		return outputData;
	}

	public CasesApiOutput processCasesApiResponse(CasesApiInput input, LocalDate dataLastRefreshedDate, LocalDate date)
	{
		CasesApiInputRow[] inputRows = input.getRows();
		CasesApiOutputRow[] outputRows = new CasesApiOutputRow[BOROUGHS.length];

		//get the output rows
		for (int i = 0; i < BOROUGHS.length; i++)
		{
			outputRows[i] = produceOutputRow(inputRows, BOROUGHS[i]);
		}

		//populate relative danger percentages
		for (int i = 0; i < BOROUGHS.length; i++)
		{
			outputRows[i].setRelative_danger_percentage(
					calculateDangerPercentage(outputRows, outputRows[i].getAbsolute_danger_value())
			);
		}

		return new CasesApiOutput(outputRows, dataLastRefreshedDate, date);
	}

	private CasesApiOutputRow produceOutputRow(CasesApiInputRow[] inputRows, String borough)
	{

		int totalCases = Arrays.stream(inputRows)
				.filter(row -> row.getArea_name().equals(borough))
				.max(Comparator.comparing(CasesApiInputRow::getDate))
				.orElseThrow(() -> new IllegalStateException("Cannot find latest cases input row for borough [" + borough + "]"))
				.getTotal_cases();

		int casesInPastTwoWeeks = Arrays.stream(inputRows)
				.filter(row -> row.getArea_name().equals(borough))
				.mapToInt(CasesApiInputRow::getNew_cases)
				.sum();

		int recordsCount = Math.toIntExact(
				Arrays.stream(inputRows)
						.filter(row -> row.getArea_name().equals(borough))
						.count()
		);

		if (recordsCount < 14)
		{
			//Occurs when user requests too early date e.g 20-02-20 which is not enough to calculate 2 weeks of data
			throw new GeneralUserVisibleException("Not enough data for this date", HttpStatus.NOT_FOUND);
		}
		else if (recordsCount != 14)
		{
			throw new IllegalStateException("Record count for Borough [" + borough + "] is not 14 after filtering");
		}

		double populationDensityPerSqKM = getPopulationDensityForBorough(borough);

		BigDecimal dangerValue = calculateAbsoluteDangerValue(casesInPastTwoWeeks, populationDensityPerSqKM);

		//danger percentage populated after this method called
		return new CasesApiOutputRow(borough, dangerValue, 0, totalCases, casesInPastTwoWeeks, populationDensityPerSqKM);
	}

	/**
	 * danger percentage =(dangerValueOfBorough/maxDangerValue * 100 )
	 */
	private double calculateDangerPercentage(CasesApiOutputRow[] outputRows, BigDecimal dangerValueOfBorough)
	{
		BigDecimal maxDanger_value = Arrays.stream(outputRows)
				.max(Comparator.comparing(CasesApiOutputRow::getAbsolute_danger_value))
				.orElseThrow(() -> new IllegalStateException("Cannot find max danger value of boroughs"))
				.getAbsolute_danger_value();

		return dangerValueOfBorough
				.divide(maxDanger_value, 4, RoundingMode.HALF_EVEN)
				.multiply(BigDecimal.valueOf(100))
				.doubleValue();
	}

	/**
	 * absolute danger value  = cases in past two weeks * population density
	 */
	private BigDecimal calculateAbsoluteDangerValue(int casesPastTwoWeeks, double populationDensity)
	{
		BigDecimal bd_cases = BigDecimal.valueOf(casesPastTwoWeeks);
		BigDecimal bd_populationDensity = BigDecimal.valueOf(populationDensity);

		return bd_cases
				.multiply(bd_populationDensity);
	}

	private double getPopulationDensityForBorough(String borough)
	{
		double boroughPopDensity;

		if (borough.equalsIgnoreCase("Hackney and City of London"))
		{
			PopulationDensityRecord popDensityRecordHackney = populationDensityDataAccessService
					.getPopulationDensityRecordForBoroughForCurrentYear("Hackney");

			PopulationDensityRecord popDensityRecordCOL = populationDensityDataAccessService
					.getPopulationDensityRecordForBoroughForCurrentYear("City of London");

			double areaHackney = popDensityRecordHackney.getSquareKilometres();
			double areaCOL = popDensityRecordCOL.getSquareKilometres();
			double combinedArea = areaHackney + areaCOL;

			double popDensityHackney = popDensityRecordHackney.getPopulationPerSquareKilometre();
			double popDensityCOL = popDensityRecordCOL.getPopulationPerSquareKilometre();

			//combine the population densities acc. to area sizes
			boroughPopDensity = (popDensityHackney * areaHackney / combinedArea) + (popDensityCOL * areaCOL / combinedArea);
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
