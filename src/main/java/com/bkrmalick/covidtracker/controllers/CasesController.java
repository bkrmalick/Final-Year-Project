package com.bkrmalick.covidtracker.controllers;

import com.bkrmalick.covidtracker.models.cases_api.output.CasesApiOutput;
import com.bkrmalick.covidtracker.services.CasesProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;


@RestController
@RequestMapping("/api/v1/cases")
@CrossOrigin(origins= {"localhost","https://covidtracker.london"})
public class CasesController
{
	private static final Logger logger = LoggerFactory.getLogger(CasesController.class);
	private final CasesProcessingService casesProcessingService;

	@Autowired
	public CasesController( CasesProcessingService casesProcessingService)
	{
		this.casesProcessingService=casesProcessingService;
	}

	@GetMapping
	public CasesApiOutput getCasesApiOutput()
	{
		logger.info("INCOMING REQUEST FOR MOST RECENT CASES DATA");
		//if no date path variable has been provided, just get most recent data
		return casesProcessingService.produceOutputResponse(null);
	}

	@GetMapping("/{date}")
	public CasesApiOutput getCasesApiOutputDate(@PathVariable("date") @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate date)
	{
		logger.info("INCOMING REQUEST FOR DATE ["+date+"]");
		return casesProcessingService.produceOutputResponse(date);
	}

}
