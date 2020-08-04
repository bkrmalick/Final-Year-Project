package com.bkrmalick.covidtracker.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApiConfig
{
	@Value("${cases-api-url}")
	private String casesApiURL;

	@Value("${postcode-api-url}")
	private String postCodeApiURL;

	/*create a single RestTemplate() instance bean to use for external API's ingestion*/
	@Bean
	public RestTemplate getRestTemplate()
	{
		return new RestTemplate();
	}

	/*create single String bean for CasesApiURL (need to qualify as Bean is one of two String level beans)*/
	@Bean
	@Qualifier("casesApiURL")
	public String getCasesApiURL()
	{
		return casesApiURL;
	}

	/*create single String bean for postCodeApiURL (need to qualify as Bean is one of two String level beans)*/
	@Bean
	@Qualifier("postCodeApiURL")
	public String getPostCodeApiURL()
	{
		return postCodeApiURL;
	}
}
