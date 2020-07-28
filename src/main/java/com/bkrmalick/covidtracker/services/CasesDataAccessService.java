package com.bkrmalick.covidtracker.services;

import com.bkrmalick.covidtracker.models.cases_api.input.CasesApiInput;
import com.bkrmalick.covidtracker.models.cases_api.output.CasesApiOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Repository
public class CasesDataAccessService
{
	private String apiURL;
	private RestTemplate restTemplate;

	@Autowired
	public CasesDataAccessService(String apiURL, RestTemplate restTemplate)
	{
		this.apiURL = apiURL;
		this.restTemplate = restTemplate;
	}

	public CasesApiInput getDataForTwoWeeksBeforeDate(Date date)
	{
		String sql="select area_name,area_code,\"date\",new_cases,total_cases FROM dataset WHERE date > (cast('"+date+"' as date) - interval '14 day') order by area_name desc offset 0 limit 500;";
		CasesApiInput responseReceived=restTemplate.getForObject(apiURL+sql, CasesApiInput.class );

		return responseReceived;
	}

	public Date getLastRefreshDate()
	{
		CasesApiInput responseReceived= restTemplate.getForObject(apiURL+"select max(date) as date FROM dataset;", CasesApiInput.class);;
		return responseReceived.getRows()[0].getDate();
	}
}
