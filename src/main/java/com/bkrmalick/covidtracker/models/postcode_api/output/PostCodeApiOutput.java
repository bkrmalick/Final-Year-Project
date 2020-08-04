package com.bkrmalick.covidtracker.models.postcode_api.output;

/**
 * Details spat out by post code API
 */
public class PostCodeApiOutput
{
	private String borough;

	public PostCodeApiOutput()
	{
		//no need to do anything default values already set for instance variables
	}

	public String getBorough()
	{
		return borough;
	}

	public void setBorough(String borough)
	{
		this.borough = borough;
	}

	public PostCodeApiOutput(String borough)
	{
		this.borough = borough;
	}
}
