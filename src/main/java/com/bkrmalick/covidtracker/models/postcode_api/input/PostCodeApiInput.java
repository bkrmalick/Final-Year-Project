package com.bkrmalick.covidtracker.models.postcode_api.input;

import java.util.LinkedHashMap;

/**
 * Details ingested by postcode API
 */
public class PostCodeApiInput
{
	private String status;
	private LinkedHashMap<String, Object> result;

	public PostCodeApiInput()
	{
		//no need to do anything default values already set for instance variables
	}

	public PostCodeApiInput(String status, LinkedHashMap<String, Object> result)
	{
		this.status = status;
		this.result = result;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public LinkedHashMap<String, Object> getResult()
	{
		return result;
	}

	public void setResult(LinkedHashMap<String, Object> result)
	{
		this.result = result;
	}
}
