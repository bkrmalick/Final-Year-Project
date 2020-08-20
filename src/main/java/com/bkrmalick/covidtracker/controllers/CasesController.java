package com.bkrmalick.covidtracker.controllers;

import com.bkrmalick.covidtracker.models.cases_api.output.CasesApiOutput;
import com.bkrmalick.covidtracker.services.CasesProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
