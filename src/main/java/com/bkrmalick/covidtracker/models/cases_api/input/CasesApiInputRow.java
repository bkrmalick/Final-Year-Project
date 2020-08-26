package com.bkrmalick.covidtracker.models.cases_api.input;

import java.time.LocalDate;

public class CasesApiInputRow //implements Comparable<CasesApiInputRow>
{
	private String area_name;
	private String area_code;
	private LocalDate date;
	private int new_cases;
	private int total_cases;

	public CasesApiInputRow()
	{
		//no need to do anything default values already set for instance variables
	}

	public CasesApiInputRow(String area_name, String area_code, LocalDate date, int new_cases, int total_cases)
	{
		this.area_name = area_name;
		this.area_code = area_code;
		this.date = date;
		this.new_cases = new_cases;
		this.total_cases = total_cases;
	}

	public String getArea_name()
	{
		return area_name;
	}

	public void setArea_name(String area_name)
	{
		this.area_name = area_name;
	}

	public String getArea_code()
	{
		return area_code;
	}

	public void setArea_code(String area_code)
	{
		this.area_code = area_code;
	}

	public LocalDate getDate()
	{
		return date;
	}

	public void setDate(LocalDate date)
	{
		this.date = date;
	}

	public int getNew_cases()
	{
		return new_cases;
	}

	public void setNew_cases(int new_cases)
	{
		this.new_cases = new_cases;
	}

	public int getTotal_cases()
	{
		return total_cases;
	}

	public void setTotal_cases(int total_cases)
	{
		this.total_cases = total_cases;
	}

	/*
	@Override
	public int compareTo(CasesApiInputRow other)
	{
		retur
	}*/
}
