package com.bkrmalick.covidtracker.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.script.ScriptException;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

/**
 * This class also handles any GeneralUserVisibleException's which are manually thrown by the programmer
 */
@ControllerAdvice
public class ApiExceptionHandler
{
	@ExceptionHandler(value={GeneralUserVisibleException.class})
	public ResponseEntity<Object> handleApiRequestException(GeneralUserVisibleException e)
	{
		//1. Create payload containing exception details
		ApiExceptionDescriber z= new ApiExceptionDescriber(
				e.getMessage(),
				e.getStatusCode(),
				ZonedDateTime.now(ZoneId.of("Europe/London")));

		//2. Return response entity
		return new ResponseEntity<>(z,e.getStatusCode());
	}

	@ExceptionHandler(value={MethodArgumentTypeMismatchException.class})
	public ResponseEntity<Object> handleApiRequestException(MethodArgumentTypeMismatchException e)
	{
		//1. Create payload containing exception details
		ApiExceptionDescriber z= new ApiExceptionDescriber(
				"Please ensure input is a valid "+e.getRequiredType().getSimpleName(),
				HttpStatus.BAD_REQUEST,
				ZonedDateTime.now(ZoneId.of("Europe/London")));

		//2. Return response entity
		return new ResponseEntity<>(z,HttpStatus.BAD_REQUEST);
	}

}
