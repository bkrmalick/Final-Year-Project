package com.bkrmalick.covidtracker;

import com.bkrmalick.covidtracker.controllers.CasesController;
import com.bkrmalick.covidtracker.controllers.FormController;
import com.bkrmalick.covidtracker.controllers.PostCodeController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CovidtrackerApplicationTests
{

	@Autowired
	private CasesController c1;
	@Autowired
	private FormController c2;
	@Autowired
	private PostCodeController c3;

	@LocalServerPort
	protected int port;

	@Autowired
	private TestRestTemplate restTemplate;


	@Test
	void test_context_loads_with_all_controllers()
	{
		assertThat(this.c1).isNotNull();
		assertThat(this.c2).isNotNull();
		assertThat(this.c3).isNotNull();
	}


	@Test
	public void test_cases_api_response() throws Exception
	{
		JSONObject jsonObject = this.getCasesAPIResponseWithoutDate();
		JSONArray cases_data = jsonObject.getJSONArray("cases_data");
		assertThat(cases_data).isNotNull();
	}

	@Test
	public void test_form_api_response() throws Exception
	{
		JSONObject jsonObject = this.getFormQuestions();
		JSONArray questions = jsonObject.getJSONArray("questions");
		assertThat(questions).isNotNull();
	}

	@Test
	public void test_future_date() throws Exception
	{
		String futureDate = LocalDate
				.now()
				.plusDays(1)
				.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

		JSONObject jsonObject = this.getCasesAPIResponseWithDate(futureDate);
		String mode = jsonObject.getString("mode");

		assertThat(mode).isEqualTo("Prediction");
	}

	@Test
	public void test_postcode_to_borough_translation() throws Exception
	{
		JSONObject response = getBoroughForPostCode("WC1E 7HX"); // Birkbeck's post code
		String borough = response.getString("borough");

		assertThat(borough).isEqualTo("Camden");
	}

	@Test
	public void test_past_date() throws Exception
	{
		JSONObject jsonObject = this.getCasesAPIResponseWithDate("01-05-2021");
		String mode = jsonObject.getString("mode");

		assertThat(mode).isEqualTo("Analysis");
	}

	@Test
	public void test_invalid_date() throws Exception
	{
		JSONObject jsonObject = this.getCasesAPIResponseWithDate("01-14-2021");
		String httpStatus = jsonObject.getString("httpStatus");
		assertThat(httpStatus).isEqualTo("BAD_REQUEST");
	}

	@Test
	public void test_date_for_which_no_data() throws Exception
	{
		JSONObject jsonObject = this.getCasesAPIResponseWithDate("01-01-2019");
		String httpStatus = jsonObject.getString("httpStatus");
		assertThat(httpStatus).isEqualTo("NOT_FOUND");
	}


	private JSONObject getCasesAPIResponseWithDate(String date) throws JSONException
	{

		String response =
				this.restTemplate.getForObject("http://localhost:" + this.port + "/api/v1/cases/" + date, String.class);

		return new JSONObject(response);
	}

	private JSONObject getCasesAPIResponseWithoutDate() throws JSONException
	{

		String response =
				this.restTemplate.getForObject("http://localhost:" + this.port + "/api/v1/cases/", String.class);

		return new JSONObject(response);
	}

	private JSONObject getBoroughForPostCode(String postcode) throws JSONException
	{

		String response =
				this.restTemplate.getForObject("http://localhost:" + this.port + "/api/v1/postcode/" + postcode, String.class);

		return new JSONObject(response);
	}

	private JSONObject getFormQuestions() throws JSONException
	{
		String response =
				this.restTemplate.getForObject("http://localhost:" + this.port + "/api/v1/form/questions", String.class);

		return new JSONObject(response);
	}
}
