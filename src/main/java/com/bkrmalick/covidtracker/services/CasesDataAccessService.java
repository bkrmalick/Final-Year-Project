package com.bkrmalick.covidtracker.services;

import com.bkrmalick.covidtracker.exceptions.GeneralUserVisibleException;
import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Repository
public class CasesDataAccessService
{
	private String apiURL;
	private RestTemplate restTemplate;

	@Autowired
	public CasesDataAccessService(@Qualifier("casesApiURL") String apiURL, RestTemplate restTemplate)
	{
		this.apiURL = apiURL;
		this.restTemplate = restTemplate;
	}

	public CasesApiInput getDataForDaysBeforeDate(LocalDate date, int days)
	{
		String sql="select area_name,area_code,\"date\",new_cases,total_cases FROM dataset "
				+"WHERE date > (cast('"+date.format(DateTimeFormatter.BASIC_ISO_DATE)+"' as date) - interval '"+days+" day') "
				+"and  date <= cast('"+date.format(DateTimeFormatter.BASIC_ISO_DATE)+"' as date) "
				+"order by area_name desc offset 0 limit 500;"; //14*32 will always be lesser than 500


		//response will have 14 records * 32 boroughs = 448 TODO add assert for responseReceived.getRows().length
		CasesApiInput responseReceived = restTemplate.getForObject(apiURL + sql, CasesApiInput.class);

		if(responseReceived.getRows().length==0)
		{
			throw new GeneralUserVisibleException("No data found for this date", HttpStatus.NOT_FOUND);
		}

		//not catching the error or throwing a user visible exception here as we do not want to expose the query to the client
		//TODO logging for exceptions e.g when changing to area_name1

		return responseReceived;
	}

	public LocalDate getDataLastRefreshedDate()
	{
		CasesApiInput responseReceived= restTemplate.getForObject(apiURL+"select max(date) as date FROM dataset;", CasesApiInput.class);;
		return responseReceived.getRows()[0].getDate();
	}

	public CasesApiInput getAllDataForBorough(String borough)
	{
		CasesApiInput responseReceived= restTemplate.getForObject(apiURL+
						"select \"date\",total_cases FROM dataset " +
						"WHERE area_name like '"+borough+"'"+
						"offset 0 limit 5000;"
				, CasesApiInput.class); //todo remove limit?

		if(responseReceived.getRows().length==0)
			throw new GeneralUserVisibleException("No data found for borough [" +borough+ "]", HttpStatus.NOT_FOUND);

		return responseReceived;
	}
}
