package com.bkrmalick.covidtracker.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.bkrmalick.covidtracker.models.dynamo_db.PopulationDensityRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class PopulationDensityDataAccessService
{
	DynamoDBMapper dynamoDBMapper;

	@Autowired
	public PopulationDensityDataAccessService(DynamoDBMapper dynamoDBMapper)
	{
		this.dynamoDBMapper=dynamoDBMapper;
	}

	List<PopulationDensityRecord> getPopulationDensityRecordsForCurrentYear()
	{
		String currentYear = Year.now().toString();

		Map<String, AttributeValue>  eav = new HashMap<>();
		eav.put(":year",new AttributeValue().withN(currentYear));

		DynamoDBScanExpression scanExpression= new DynamoDBScanExpression()
				.withFilterExpression("Year = :year")
				.withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(PopulationDensityRecord.class,scanExpression);
	}

	PopulationDensityRecord getPopulationDensityRecordForBoroughForCurrentYear(String borough)
	{
		String currentYear = Year.now().toString();

		//map for storing values which need to be looked up
		Map<String, AttributeValue>  eav = new HashMap<>();
		eav.put(":boroughVal",new AttributeValue().withS(borough));
		eav.put(":yearVal",new AttributeValue().withN(currentYear));

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
			throw new IllegalStateException("Population Density Record not found for Borough ["+borough+"] and year ["+currentYear+"]");
		}

		//otherwise return first record from result
		return itr.next();
	}
}
