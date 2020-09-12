package com.bkrmalick.covidtracker.services;

import com.bkrmalick.covidtracker.models.postcode_api.output.PostCodeApiOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostCodeProcessingService
{
	private static final Logger logger = LoggerFactory.getLogger(PostCodeProcessingService.class);
	private PostCodeDataAccessService postCodeDataAccessService;

	@Autowired
	public PostCodeProcessingService(PostCodeDataAccessService postCodeDataAccessService)
	{
		this.postCodeDataAccessService = postCodeDataAccessService;
	}

	public PostCodeApiOutput produceOutputResponse(String postCode)
	{
		logger.info("REQUEST OF BOROUGH FOR POSTCODE ["+postCode+"]");
		String borough=null;

		borough=postCodeDataAccessService.getBoroughForPostCode(postCode);

		if(borough.equalsIgnoreCase("City of London") || borough.equalsIgnoreCase("Hackney"))
			borough="Hackney and City of London";

		PostCodeApiOutput output=new PostCodeApiOutput(borough);

		return output;
	}
}
