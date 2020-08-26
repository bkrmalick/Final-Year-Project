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
		System.out.println("CALL CASES");
		return casesProcessingService.produceOutputResponse();
	}

	@GetMapping("/{date}")
	public LocalDate getCasesApiOutput(@PathVariable("date") @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate date)
	{
		System.out.println(date);
		return date;
	}

}
