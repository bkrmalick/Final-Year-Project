package com.bkrmalick.covidtracker.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.bkrmalick.covidtracker.exceptions.GeneralUserVisibleException;
import com.bkrmalick.covidtracker.models.dynamo_db.FormQuestionRecord;
import com.bkrmalick.covidtracker.util.DateTimeUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class FormDataAccessService
{
	private static final Logger logger = LoggerFactory.getLogger(FormDataAccessService.class);
	private final Table formResponseTable;
	private final DynamoDBMapper dynamoDBMapper;

	@Autowired
	public FormDataAccessService(@Qualifier("formResponseTable") Table formResponseTable , DynamoDBMapper dynamoDBMapper)
	{
		this.dynamoDBMapper = dynamoDBMapper;
		this.formResponseTable=formResponseTable;
	}

	@Cacheable(value = "formQuestions") //cache expiry defined in CachingConfig
	public List<FormQuestionRecord> getQuestions()
	{
		logger.info("Getting form questions from the database");
		return dynamoDBMapper.scan(FormQuestionRecord.class, new DynamoDBScanExpression());
	}

	/**
	 * Saves map of answers to the db as JSON object
	 * Uses the Table entity directly instead of dynamoDB mapper as it allows saving to JSON data-type.
	 * @param answers
	 */
	public void saveAnswers(Map<String,String> answers)
	{
		String jsonAnswers= new JSONObject(answers).toString();
		String id=UUID.randomUUID().toString();
		String createdDate = DateTimeUtils.getDateTimeNowInUTC().toString();

		Item item =new Item()
				.withPrimaryKey("id", id)
				.withJSON("answersMap", jsonAnswers)
				.withString("createdDate", createdDate);
		try
		{
			formResponseTable.putItem(item);
			logger.info("saved to db response object ["+item.toJSON()+"]");
		}
		catch(Exception e)
		{
			logger.error("Failed to store response object ["+item.toJSON()+"]",e);
			throw new GeneralUserVisibleException("There was an error storing the response, please try again later or contact admin! Reference: "+id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
