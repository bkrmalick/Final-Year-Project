package com.bkrmalick.covidtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * Main Class that launches spring container
 *
 * @author Abu Bakar Naseer
 */
@SpringBootApplication
@EnableCaching
public class Main
{
	public static void main(String[] args)
	{
		SpringApplication.run(Main.class, args);
	}
}
