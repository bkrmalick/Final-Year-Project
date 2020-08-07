import axios from "axios"

const API_URL = "/"; //localhost

export function getCasesData(event) 
{
     return axios.get(API_URL +`api/v1/cases`);
}

export function getBoroughForPostCode(postCode)
{
     return axios.get(API_URL +`api/v1/postcode/`+postCode);
}