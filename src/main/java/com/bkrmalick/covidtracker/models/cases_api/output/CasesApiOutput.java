package com.bkrmalick.covidtracker.models.cases_api.output;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class CasesApiOutput
{
	@JsonFormat(pattern="dd-MM-yyyy")
	private LocalDate date; //Date for which the output has been returned

	@JsonFormat(pattern="dd-MM-yyyy")
	private LocalDate data_last_refreshed;

	private CasesApiOutputRow[] cases_data;

	public CasesApiOutput(CasesApiOutputRow[] cases_data, LocalDate data_last_refreshed, LocalDate date)
	{
		this.cases_data = cases_data;
		this.data_last_refreshed = data_last_refreshed;
		this.date=date;
	}

	public LocalDate getData_last_refreshed()
	{
		return data_last_refreshed;
	}

	public void setData_last_refreshed(LocalDate data_last_refreshed)
	{
		this.data_last_refreshed = data_last_refreshed;
	}

	public CasesApiOutputRow[] getCases_data()
	{
		return cases_data;
	}

	public void setCases_data(CasesApiOutputRow[] cases_data)
	{
		this.cases_data = cases_data;
	}

	public LocalDate getDate()
	{
		return date;
	}

	public void setDate(LocalDate date)
	{
		this.date = date;
	}
}
