package com.bkrmalick.covidtracker.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.bkrmalick.covidtracker.exceptions.GeneralUserVisibleException;
import com.bkrmalick.covidtracker.models.dynamo_db.FormQuestionRecord;
import com.bkrmalick.covidtracker.models.dynamo_db.FormResponseRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class FormDataAccessService
{
	private static final Logger logger = LoggerFactory.getLogger(FormDataAccessService.class);
	private final DynamoDBMapper dynamoDBMapper;

	@Autowired
	public FormDataAccessService(DynamoDBMapper dynamoDBMapper)
	{
		this.dynamoDBMapper = dynamoDBMapper;
	}

	@Cacheable(value = "formQuestions") //cache expiry defined in CachingConfig
	public List<FormQuestionRecord> getQuestions()
	{
		logger.info("Getting form questions from the database");
		return dynamoDBMapper.scan(FormQuestionRecord.class, new DynamoDBScanExpression());
	}

	public FormResponseRecord saveAnswers(Map<String,String> answers)
	{
		FormResponseRecord item = new FormResponseRecord(answers);

		try
		{
			dynamoDBMapper.save(item);
			logger.info("saved to db response object ["+item+"]");
		}
		catch(Exception e)
		{
			logger.error("Failed to store response object ["+item+"]",e);
			throw new GeneralUserVisibleException("There was an error storing the response, please try again later or contact admin! Response Reference: "+item.getId(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return item;
	}
}
