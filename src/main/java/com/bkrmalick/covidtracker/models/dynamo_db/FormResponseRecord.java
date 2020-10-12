package com.bkrmalick.covidtracker.models.dynamo_db;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@DynamoDBTable(tableName = "covidtracker.form-response")
public class FormResponseRecord
{
	private String id;
	private String answersMap; //todo store as document?
	private String createdDate;

	public FormResponseRecord()
	{
	}

	public FormResponseRecord(Map<String,String> answersMap)
	{
		this.id = UUID.randomUUID().toString();
		this.answersMap = new JSONObject(answersMap).toString();;
		this.createdDate = ZonedDateTime.now(ZoneId.of("Europe/London")).toString();
	}

	@DynamoDBHashKey
	@DynamoDBAutoGeneratedKey
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	@DynamoDBAttribute
	public String getAnswersMap()
	{
		return answersMap;
	}

	public void setAnswersMap(String answersMap)
	{
		this.answersMap = answersMap;
	}

	@DynamoDBAttribute
	public String getCreatedDate()
	{
		return createdDate;
	}

	public void setCreatedDate(String createdDate)
	{
		this.createdDate = createdDate;
	}
}
