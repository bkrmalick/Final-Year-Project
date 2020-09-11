package com.bkrmalick.covidtracker.models.cases_api.output;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CasesApiOutputRow
{
	private String area_name;
	private BigDecimal absolute_danger_value;
	private double relative_danger_percentage;
	private int total_cases;
	private int cases_in_past_2_wks;
	private double population_per_sq_km;

	public CasesApiOutputRow(String area_name, BigDecimal absolute_danger_value, double relative_danger_percentage, int total_cases, int cases_in_past_2_wks, double population_per_sq_km)
	{
		this.area_name = area_name;
		this.population_per_sq_km = roundToOneDecimalPlaces(population_per_sq_km);
		this.total_cases = total_cases;
		this.cases_in_past_2_wks = cases_in_past_2_wks;
		this.absolute_danger_value = absolute_danger_value.setScale(2, RoundingMode.HALF_UP); //set precision to two decimal places
		this.relative_danger_percentage = relative_danger_percentage;
	}

	public String getArea_name()
	{
		return area_name;
	}

	public void setArea_name(String area_name)
	{
		this.area_name = area_name;
	}

	public double getRelative_danger_percentage()
	{
		return relative_danger_percentage;
	}

	public void setRelative_danger_percentage(double relative_danger_percentage)
	{
		this.relative_danger_percentage = relative_danger_percentage;
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

	public BigDecimal getAbsolute_danger_value()
	{
		return absolute_danger_value;
	}

	public void setAbsolute_danger_value(BigDecimal absolute_danger_value)
	{
		this.absolute_danger_value = absolute_danger_value;
	}

	private double roundToOneDecimalPlaces(double value)
	{
		return Math.round(value * 10.0) / 10.0;
	}
}
