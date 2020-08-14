package com.bkrmalick.covidtracker.models.cases_api.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class CasesApiOutput
{
	@JsonFormat(pattern="dd-MM-yyyy")
	private Date lastRefreshDate;

	private CasesApiOutputRow[] rows;

	public CasesApiOutput(CasesApiOutputRow[] rows, Date lastRefreshDate)
	{
		this.rows = rows;
		this.lastRefreshDate = lastRefreshDate;
	}

	public Date getLastRefreshDate()
	{
		return lastRefreshDate;
	}

	public void setLastRefreshDate(Date lastRefreshDate)
	{
		this.lastRefreshDate = lastRefreshDate;
	}

	public CasesApiOutputRow[] getRows()
	{
		return rows;
	}

	public void setRows(CasesApiOutputRow[] rows)
	{
		this.rows = rows;
	}

}
