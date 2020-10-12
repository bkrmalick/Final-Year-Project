package com.bkrmalick.covidtracker.models.dynamo_db;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.io.Serializable;

@DynamoDBTable(tableName = "covidtracker.form-question")
public class FormQuestionRecord implements Serializable
{
	private int id;
	private int type;
	private String text, falseText, trueText;
	private double timeout;
	private boolean exitOnFalse;

	public FormQuestionRecord()
	{

	}

	public FormQuestionRecord(int id, int type, String text, String falseText, String trueText, double timeout, boolean exitOnFalse)
	{
		this.id = id;
		this.type = type;
		this.text = text;
		this.falseText = falseText;
		this.trueText = trueText;
		this.timeout = timeout;
		this.exitOnFalse = exitOnFalse;
	}

	@DynamoDBHashKey
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	@DynamoDBAttribute
	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	@DynamoDBAttribute
	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	@DynamoDBAttribute
	public String getFalseText()
	{
		return falseText;
	}

	public void setFalseText(String falseText)
	{
		this.falseText = falseText;
	}

	@DynamoDBAttribute
	public String getTrueText()
	{
		return trueText;
	}

	public void setTrueText(String trueText)
	{
		this.trueText = trueText;
	}

	@DynamoDBAttribute
	public double getTimeout()
	{
		return timeout;
	}

	public void setTimeout(double timeout)
	{
		this.timeout = timeout;
	}

	@DynamoDBAttribute
	public boolean isExitOnFalse()
	{
		return exitOnFalse;
	}

	public void setExitOnFalse(boolean exitOnFalse)
	{
		this.exitOnFalse = exitOnFalse;
	}
}
