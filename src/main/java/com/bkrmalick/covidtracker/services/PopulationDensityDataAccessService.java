package com.bkrmalick.covidtracker.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.bkrmalick.covidtracker.models.dynamo_db.PopulationDensityRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Cacheable methods MUST
 * 			+ be public
 * 			+ only be called through proxy
 */
@Repository
public class PopulationDensityDataAccessService
{
	private static final Logger logger = LoggerFactory.getLogger(PopulationDensityDataAccessService.class);
	private final DynamoDBMapper dynamoDBMapper;
	private PopulationDensityDataAccessService proxy;

	@Autowired
	public PopulationDensityDataAccessService(DynamoDBMapper dynamoDBMapper)
	{
		this.dynamoDBMapper=dynamoDBMapper;
	}

	@Autowired
	public void setProxy(PopulationDensityDataAccessService proxy)
	{
		this.proxy = proxy;
	}

	@Cacheable(value="populationDensity", key ="#borough+#year")
	public PopulationDensityRecord getPopulationDensityRecordForBoroughForYear(String borough, String year)
	{
		logger.info("Getting Pop. Density From DynamoDB for ["+borough+"] for year ["+year+"]");

		//map for storing values which need to be looked up
		Map<String, AttributeValue>  eav = new HashMap<>();
		eav.put(":boroughVal",new AttributeValue().withS(borough));
		eav.put(":yearVal",new AttributeValue().withN(year));

		//have to alias the table column names since they are reserved keywords
		Map<String, String> attributeAliases = new HashMap<>();
		attributeAliases.put("#y", "Year");
		attributeAliases.put("#n", "Name");

		DynamoDBQueryExpression<PopulationDensityRecord> scanExpression=
				new DynamoDBQueryExpression<PopulationDensityRecord>()
					.withKeyConditionExpression("#n = :boroughVal and #y = :yearVal")
					.withExpressionAttributeValues(eav)
					.withExpressionAttributeNames(attributeAliases);


		List<PopulationDensityRecord> result=dynamoDBMapper.query(PopulationDensityRecord.class,scanExpression);

		Iterator<PopulationDensityRecord> itr =result.iterator();

		//throw exception if no record found
		if(!itr.hasNext())
		{
			throw new IllegalStateException("Population Density Record not found for Borough ["+borough+"] and year ["+year+"]");
		}

		//otherwise return first record from result
		return itr.next();
	}

	PopulationDensityRecord getPopulationDensityRecordForBoroughForCurrentYear(String borough)
	{
		String currentYear = Year.now().toString();

		return proxy.getPopulationDensityRecordForBoroughForYear(borough, currentYear);
	}
}
