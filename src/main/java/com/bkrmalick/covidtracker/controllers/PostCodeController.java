package com.bkrmalick.covidtracker.controllers;

import com.bkrmalick.covidtracker.models.postcode_api.output.PostCodeApiOutput;
import com.bkrmalick.covidtracker.services.PostCodeProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/postcode")
@CrossOrigin("localhost")
public class PostCodeController
{
	private final PostCodeProcessingService postCodeProcessingService;

	@Autowired
	public PostCodeController(PostCodeProcessingService postCodeProcessingService)
	{
		this.postCodeProcessingService = postCodeProcessingService;
	}

	@GetMapping(path="{postCode}")
	public PostCodeApiOutput getPostCodeApiOutput(@PathVariable(value = "postCode") String postCode)
	{
		return postCodeProcessingService.produceOutputResponse(postCode);
	}

	@GetMapping
	public PostCodeApiOutput getPostCodeApiOutputWithoutParameter()
	{
		return getPostCodeApiOutput("");
	}
}
