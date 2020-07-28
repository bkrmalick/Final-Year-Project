package com.bkrmalick.covidtracker.models.cases_api.input;

import java.util.LinkedHashMap;

public class CasesApiInput
{
	private LinkedHashMap<String, String> info;
	private LinkedHashMap<String, String>[] fields;
	private CasesApiInputRow[] rows;

	public CasesApiInput()
	{
		//no need to do anything default values already set for instance variables
	}
	public CasesApiInput(LinkedHashMap<String, String> info, LinkedHashMap<String, String>[] fields, CasesApiInputRow[] rows)
	{
		this.info = info;
		this.fields = fields;
		this.rows = rows;
	}

	public LinkedHashMap<String, String> getInfo()
	{
		return info;
	}

	public void setInfo(LinkedHashMap<String, String> info)
	{
		this.info = info;
	}

	public LinkedHashMap<String, String>[] getFields()
	{
		return fields;
	}

	public void setFields(LinkedHashMap<String, String>[] fields)
	{
		this.fields = fields;
	}

	public void setRows(CasesApiInputRow[] rows)
	{
		this.rows = rows;
	}

	public CasesApiInputRow[] getRows()
	{
		return rows;
	}
}
