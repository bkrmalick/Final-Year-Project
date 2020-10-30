package com.bkrmalick.covidtracker.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtils
{
	public static ZonedDateTime getDateTimeNowInUTC()
	{
		return ZonedDateTime.now(ZoneId.of("UTC"));
	}
}
