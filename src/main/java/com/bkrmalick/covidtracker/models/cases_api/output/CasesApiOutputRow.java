package com.bkrmalick.covidtracker.models.cases_api.output;

public class CasesApiOutputRow
{
	private String area_name;
	private double danger_percentage;
	private int total_cases;
	private int cases_in_past_2_wks;
	private double population_per_sq_km;

	public CasesApiOutputRow(String area_name, double danger_percentage, int total_cases, int cases_in_past_2_wks, double population_per_sq_km)
	{
		this.area_name = area_name;
		this.danger_percentage = danger_percentage;
		this.total_cases = total_cases;
		this.cases_in_past_2_wks = cases_in_past_2_wks;
		this.population_per_sq_km = population_per_sq_km;
	}

	public String getArea_name()
	{
		return area_name;
	}

	public void setArea_name(String area_name)
	{
		this.area_name = area_name;
	}

	public double getDanger_percentage()
	{
		return danger_percentage;
	}

	public void setDanger_percentage(double danger_percentage)
	{
		this.danger_percentage = danger_percentage;
	}

	public int getTotal_cases()
	{
		return total_cases;
	}

	public void setTotal_cases(int total_cases)
	{
		this.total_cases = total_cases;
	}

	public int getCases_in_past_2_wks()
	{
		return cases_in_past_2_wks;
	}

	public void setCases_in_past_2_wks(int cases_in_past_2_wks)
	{
		this.cases_in_past_2_wks = cases_in_past_2_wks;
	}

	public double getPopulation_per_sq_km()
	{
		return population_per_sq_km;
	}

	public void setPopulation_per_sq_km(double population_per_sq_km)
	{
		this.population_per_sq_km = population_per_sq_km;
	}

}
