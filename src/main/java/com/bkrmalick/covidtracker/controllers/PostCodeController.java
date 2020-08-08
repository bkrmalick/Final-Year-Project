package com.bkrmalick.covidtracker.controllers;

import com.bkrmalick.covidtracker.models.postcode_api.output.PostCodeApiOutput;
import com.bkrmalick.covidtracker.services.PostCodeProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/postcode")
//todo CORS policy
public class PostCodeController
{
	private PostCodeProcessingService postCodeProcessingService;

	@Autowired
	public PostCodeController(PostCodeProcessingService postCodeProcessingService)
	{
		this.postCodeProcessingService = postCodeProcessingService;
	}

	@GetMapping(path="{postCode}")
	public PostCodeApiOutput getPostCodeApiOutput(@PathVariable(value = "postCode") String postCode)
	{
		System.out.println("CALL POSTCODE");
		return postCodeProcessingService.produceOutputResponse(postCode);
	}

	@GetMapping
	public PostCodeApiOutput getPostCodeApiOutputWithoutParameter()
	{
		return getPostCodeApiOutput("");
	}
}
