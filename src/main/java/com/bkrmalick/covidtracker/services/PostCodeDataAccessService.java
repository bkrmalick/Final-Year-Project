package com.bkrmalick.covidtracker.services;

import com.bkrmalick.covidtracker.models.postcode_api.input.PostCodeApiInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedHashMap;

@Repository
public class PostCodeDataAccessService
{
	private String postCodeApiURL;
	private RestTemplate restTemplate;
	private final String[] BOROUGHS;

	@Autowired
	public PostCodeDataAccessService(@Qualifier("postCodeApiURL") String postCodeApiURL, RestTemplate restTemplate, @Qualifier("BOROUGH_NAMES") String [] BOROUGHS)
	{
		this.postCodeApiURL = postCodeApiURL;
		this.restTemplate = restTemplate;
		this.BOROUGHS=BOROUGHS;
	}

	public String getBoroughForPostCode(String postCode) //throws Exception
	{
		String url=postCodeApiURL+"/"+postCode;
		String borough=null;

		LinkedHashMap<String, Object> result=restTemplate.getForObject(url,PostCodeApiInput.class).getResult();

		borough=(String) result.get("admin_district");

		if(borough==null)
			throw new IllegalStateException("key [admin_district] in ext API not found or null");
		else if(!validBorough(borough))
			throw new IllegalStateException("ext API returned invalid borough ["+borough+"]");

		return borough;
	}


	private boolean validBorough(String borough)
	{
		int length=BOROUGHS.length;

		for (String s : BOROUGHS)
		{
			if (s.equalsIgnoreCase(borough))
				return true;
		}

		return false;
	}
}
