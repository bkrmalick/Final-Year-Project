package com.bkrmalick.covidtracker.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * source: https://www.baeldung.com/spring-rest-template-error-handling
 *By default, the RestTemplate.getForObject() will throw one of these exceptions in case of failed external API call:
 * HttpClientErrorException – in case of HTTP status 4xx
 * HttpServerErrorException – in case of HTTP status 5xx
 * UnknownHttpStatusCodeException – in case of an unknown HTTP status
 *
 * This class serves to handle and return back to client the reason in-case of first two types of failures.
 * Third type is not handled as this would mean the external API has fallen over.
 */
@ControllerAdvice
public class ApiExceptionHandler
{
	@ExceptionHandler(value={HttpClientErrorException.class, HttpServerErrorException.class})
	public ResponseEntity<Object> handleApiRequestException(HttpStatusCodeException e)
	{
		HttpStatus statusCode=e.getStatusCode();

		//1. Create payload containing exception details
		ApiExceptionDescriber z= new ApiExceptionDescriber(
				e.getMessage(),
				statusCode,
				ZonedDateTime.now(ZoneId.of("Europe/London")));

		//2. Return response entity
		return new ResponseEntity<>(z,statusCode);
	}
}
