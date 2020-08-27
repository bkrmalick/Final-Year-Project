package com.bkrmalick.covidtracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class GeneralUserVisibleException extends HttpStatusCodeException
{
	public GeneralUserVisibleException(String message, HttpStatus status)
	{
		super(status, message);
	}
	public GeneralUserVisibleException(HttpStatusCodeException e)
	{
		super( e.getStatusCode(),e.getResponseBodyAsString());
	}

}
