package com.bkrmalick.covidtracker.services;

import com.bkrmalick.covidtracker.models.postcode_api.input.PostCodeApiInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

@Repository
public class PostCodeDataAccessService
{
	private String postCodeApiURL;
	private RestTemplate restTemplate;

	@Autowired
	public PostCodeDataAccessService(@Qualifier("postCodeApiURL") String postCodeApiURL, RestTemplate restTemplate)
	{
		this.postCodeApiURL = postCodeApiURL;
		this.restTemplate = restTemplate;
	}

	public String getBoroughForPostCode(String postCode) //throws Exception
	{
		String url=postCodeApiURL+"/"+postCode;
		String borough=null;

		LinkedHashMap<String, Object> result=restTemplate.getForObject(url,PostCodeApiInput.class).getResult();

		borough=(String) result.get("primary_care_trust");

		//if(borough==null)
		//	throw new NullPointerException("key [primary_care_trust] in ext API not found or null");

		return borough;
	}
}
