import axios from "axios"

const API_URL = "/"; //localhost

/* WILL GET MOST RECENT DATA */
export function getCasesData() 
{
     return axios.get(API_URL +`api/v1/cases`);
}

/* WILL GET (PREDICTED OR HISTORICAL)  DATA FOR PARTICULAR DATE */
export function getCasesDataForDate(date) 
{
     if (date === null)
          return getCasesData();

     return axios.get(API_URL +`api/v1/cases/` + date);
}

export function getBoroughForPostCode(postCode)
{
     return axios.get(API_URL +`api/v1/postcode/`+postCode);
}

export function getFormQuestions()
{
     return axios.get(API_URL +`api/v1/form/questions`);
}

export function postFormAnswers(answersMap)
{
     return axios.post(API_URL + `api/v1/form/answers`, convertMapToJSON(answersMap)) ;
}

function convertMapToJSON(map)
{
     let toReturn = {}

     map.forEach((v,k ) => toReturn[k] = v)
     return toReturn;
}