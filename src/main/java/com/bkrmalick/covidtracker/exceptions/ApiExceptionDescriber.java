package com.bkrmalick.covidtracker.exceptions;

import org.springframework.http.HttpStatus;
import java.time.ZonedDateTime;

/**
 * Class to return to api caller and describe the exception received from restTemplate.getForObject(...)
 */
public class ApiExceptionDescriber
{
	private final String message; //actually the external api response - set in ApiExceptionHandler
	private final HttpStatus httpStatus;
	private final ZonedDateTime timestamp;

	public ApiExceptionDescriber(String message, HttpStatus httpStatus, ZonedDateTime timestamp)
	{
		this.message = message;
		this.httpStatus = httpStatus;
		this.timestamp = timestamp;
	}

	public String getMessage()
	{
		return message;
	}

	public HttpStatus getHttpStatus()
	{
		return httpStatus;
	}

	public ZonedDateTime getTimestamp()
	{
		return timestamp;
	}
}
