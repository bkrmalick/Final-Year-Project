package com.bkrmalick.covidtracker.models.form_api;

import com.bkrmalick.covidtracker.models.dynamo_db.FormQuestionRecord;

import java.util.List;

public class FormQuestionApiOutput
{
	private List<FormQuestionRecord> questions;

	public FormQuestionApiOutput()
	{
	}

	public FormQuestionApiOutput(List<FormQuestionRecord> questions)
	{
		this.questions = questions;
	}

	public List<FormQuestionRecord> getQuestions()
	{
		return questions;
	}

	public void setQuestions(List<FormQuestionRecord> questions)
	{
		this.questions = questions;
	}
}
