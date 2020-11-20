package com.bkrmalick.covidtracker.controllers;

import com.bkrmalick.covidtracker.models.dynamo_db.FormResponseRecord;
import com.bkrmalick.covidtracker.models.form_api.FormQuestionApiOutput;
import com.bkrmalick.covidtracker.services.FormProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/form")
@CrossOrigin("localhost")
public class FormController
{
	private final FormProcessingService formProcessingService;

	@Autowired
	public FormController(FormProcessingService formProcessingService)
	{
		this.formProcessingService = formProcessingService;
	}

	@RequestMapping(value="/questions", method=RequestMethod.GET)
	public FormQuestionApiOutput getPostCodeApiOutput()
	{
		return formProcessingService.getQuestions();
	}

	@RequestMapping(value = "/answers", method = RequestMethod.POST, consumes = "application/json")
	public FormResponseRecord saveAnswer(@RequestBody Map<String, String> answers) //<Question,Answer>
	{
		return formProcessingService.saveAnswers(answers);
	}
}
