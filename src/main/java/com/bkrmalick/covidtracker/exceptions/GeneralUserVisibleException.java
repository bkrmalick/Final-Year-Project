package com.bkrmalick.covidtracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class GeneralUserVisibleException extends HttpStatusCodeException
{
	public GeneralUserVisibleException(String message, HttpStatus status)
	{
		super(message, status,"",null,null,null);
	}
	public GeneralUserVisibleException(HttpStatusCodeException e)
	{
		super(e.getResponseBodyAsString(), e.getStatusCode(),"",null,null,null);
	}

	@Override
	public String toString()
	{
		return this.getResponseBodyAsString();
	}
}
