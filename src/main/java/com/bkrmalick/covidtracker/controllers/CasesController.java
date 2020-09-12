package com.bkrmalick.covidtracker.controllers;

import com.bkrmalick.covidtracker.models.cases_api.output.CasesApiOutput;
import com.bkrmalick.covidtracker.services.CasesProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;


@RestController
@RequestMapping("/api/v1/cases")
@CrossOrigin("localhost")
public class CasesController
{
	private CasesProcessingService casesProcessingService;

	@Autowired
	public CasesController( CasesProcessingService casesProcessingService)
	{
		this.casesProcessingService=casesProcessingService;
	}

	@GetMapping
	public CasesApiOutput getCasesApiOutput()
	{
		//if no date path variable has been provided, just get most recent data
		return casesProcessingService.produceOutputResponse(null);
	}

	@GetMapping("/{date}")
	public CasesApiOutput getCasesApiOutputDate(@PathVariable("date") @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate date)
	{
		return casesProcessingService.produceOutputResponse(date);
	}

}
