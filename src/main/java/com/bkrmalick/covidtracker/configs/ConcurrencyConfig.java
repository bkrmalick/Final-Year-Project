package com.bkrmalick.covidtracker.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Semaphore;

@Configuration
public class ConcurrencyConfig
{
	@Value("${concurrency.prediction-service-threads}")
	private Integer predictionServiceThreads; //that can concurrently access critical region in PredictionService

	@Bean
	@Qualifier("predictionServiceSemaphore")
	public Semaphore getPredictionServiceSemaphore()
	{
		return new Semaphore(predictionServiceThreads);
	}
}
