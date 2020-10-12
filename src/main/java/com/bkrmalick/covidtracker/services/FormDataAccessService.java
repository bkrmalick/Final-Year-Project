package com.bkrmalick.covidtracker.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.bkrmalick.covidtracker.models.dynamo_db.FormQuestionRecord;
import com.bkrmalick.covidtracker.models.dynamo_db.FormResponseRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class FormDataAccessService
{
	private static final Logger logger = LoggerFactory.getLogger(FormDataAccessService.class);
	private RestTemplate restTemplate;
	DynamoDBMapper dynamoDBMapper;

	@Autowired
	public FormDataAccessService(RestTemplate restTemplate, DynamoDBMapper dynamoDBMapper)
	{
		this.restTemplate = restTemplate;
		this.dynamoDBMapper = dynamoDBMapper;
	}

	@Cacheable(value = "formQuestions") //todo expire cache?
	public List<FormQuestionRecord> getQuestions()
	{
		logger.info("Getting form-questions from db");
		return dynamoDBMapper.scan(FormQuestionRecord.class, new DynamoDBScanExpression());
	}

	public void saveAnswers(Map<String,String> answers)
	{
		dynamoDBMapper.save(new FormResponseRecord(answers));
	}
}
