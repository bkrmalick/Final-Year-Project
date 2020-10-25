package com.bkrmalick.covidtracker.services;

import com.bkrmalick.covidtracker.exceptions.GeneralUserVisibleException;
import com.bkrmalick.covidtracker.models.dynamo_db.FormResponseRecord;
import com.bkrmalick.covidtracker.models.form_api.FormQuestionApiOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FormProcessingService
{
	private final FormDataAccessService formDataAccessService;

	@Autowired
	public FormProcessingService(FormDataAccessService formDataAccessService)
	{
		this.formDataAccessService = formDataAccessService;
	}

	public FormQuestionApiOutput getQuestions()
	{
		return new FormQuestionApiOutput(formDataAccessService.getQuestions());
	}

	public FormResponseRecord saveAnswers(Map<String,String> answers)
	{
		if(answers.size()==0)
			throw new GeneralUserVisibleException("Need to store at least one answer", HttpStatus.BAD_REQUEST);

		return formDataAccessService.saveAnswers(answers);
	}
}
