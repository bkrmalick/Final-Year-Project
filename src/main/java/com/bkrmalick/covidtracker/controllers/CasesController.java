package com.bkrmalick.covidtracker.controllers;

import com.bkrmalick.covidtracker.exceptions.GeneralUserVisibleException;
import com.bkrmalick.covidtracker.models.cases_api.output.CasesApiOutput;
import com.bkrmalick.covidtracker.services.CasesProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.script.ScriptException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
	public CasesApiOutput getCasesApiOutput() throws ScriptException, IOException, URISyntaxException
	{
		//if no date path variable has been provided, just get most recent data
		return casesProcessingService.produceOutputResponse(null);
	}

	@GetMapping("/{date}")
	public CasesApiOutput getCasesApiOutputDate(@PathVariable("date") @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate date) throws ScriptException, IOException, URISyntaxException
	{
		return casesProcessingService.produceOutputResponse(date);
	}

}
