package com.bkrmalick.covidtracker.services;

import com.bkrmalick.covidtracker.models.postcode_api.output.PostCodeApiOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostCodeProcessingService
{
	private PostCodeDataAccessService postCodeDataAccessService;

	@Autowired
	public PostCodeProcessingService(PostCodeDataAccessService postCodeDataAccessService)
	{
		this.postCodeDataAccessService = postCodeDataAccessService;
	}

	public PostCodeApiOutput produceOutputResponse(String postCode)
	{
		String borough=null;

		borough=postCodeDataAccessService.getBoroughForPostCode(postCode);

		PostCodeApiOutput output=new PostCodeApiOutput(borough);

		return output;
	}
}
