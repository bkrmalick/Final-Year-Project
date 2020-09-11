package com.bkrmalick.covidtracker.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ExternalApisConfig
{
	@Value("${cases-api-url}")
	private String casesApiURL;

	@Value("${postcode-api-url}")
	private String postCodeApiURL;

	private static final String [] BOROUGHS={"Barking and Dagenham","Barnet","Bexley","Brent","Bromley","Camden","Croydon","Ealing","Enfield","Greenwich","Hackney and City of London","Hammersmith and Fulham","Haringey","Harrow","Havering","Hillingdon","Hounslow","Islington","Kensington and Chelsea","Kingston upon Thames","Lambeth","Lewisham","Merton","Newham","Redbridge","Richmond upon Thames","Southwark","Sutton","Tower Hamlets","Waltham Forest","Wandsworth","Westminster"};

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

	/*create single String bean for borough names*/
	@Bean
	@Qualifier("BOROUGH_NAMES")
	public String [] getBoroughNames()
	{
		return this.BOROUGHS;
	}
}
