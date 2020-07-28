package com.bkrmalick.covidtracker.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CasesApiConfig
{
	@Value("${cases-api-url}")
	private String apiURL;

	/*create a single RestTemplate() instance bean to use for external API's ingestion*/
	@Bean
	public RestTemplate getRestTemplate()
	{
		return new RestTemplate();
	}

	/*create single String bean for apiUrl (need to be careful with this)*/
	@Bean
	public String getApiURL()
	{
		return apiURL;
	}
}
