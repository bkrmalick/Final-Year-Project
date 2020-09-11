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