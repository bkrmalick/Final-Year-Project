package com.bkrmalick.covidtracker.services;

import com.bkrmalick.covidtracker.models.form_api.FormQuestionApiOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FormProcessingService
{
	FormDataAccessService formDataAccessService;

	@Autowired
	public FormProcessingService(FormDataAccessService formDataAccessService)
	{
		this.formDataAccessService = formDataAccessService;
	}

	public FormQuestionApiOutput getQuestions()
	{
		return new FormQuestionApiOutput(formDataAccessService.getQuestions());
	}

	public void saveAnswers(Map<String,String> answers)
	{
		formDataAccessService.saveAnswers(answers);
	}
}
