import React, {useState} from 'react';
import Popup from 'react-popup';

import './Datepicker.css'

function Datepicker(props)
{
    const [selectedDate, setSelectedDate] = useState("");
    const MAX_MONTHS_PREDICTION = 3;
    const MIN_DATE = "2020-02-01";

    //update the date state variable acc. to prop ONLY if it already doesn't have a user selected value
    if(selectedDate!==getDateFromProp())
        setSelectedDate(getDateFromProp());

    function getDateFromProp() 
    {
        const d=props.date;

        if(d===undefined || d===null) 
            return "";

        //Convert DD-MM-YYYY to YYYY-MM-DD format 
       return d.split("-").reverse().join("-");
    }

/*     function getTodayDate() 
    {
        var d = new Date(),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();
    
        if (month.length < 2) 
            month = '0' + month;
        if (day.length < 2) 
            day = '0' + day;
    
        return [year, month, day].join('-');
    } */

    function handleChange(event)
    {
        let newDate = new Date(event.target.value);   
        let maxDate= new Date(props.casesDataRefreshDate);
        maxDate= new Date(maxDate.setMonth(maxDate.getMonth() + MAX_MONTHS_PREDICTION));
        
        if (newDate === selectedDate)
            return;
            
        //don't accept inputs outside bounds (min date handled by parent component using backend message)
        if(newDate>maxDate)
        {
            Popup.alert("Sorry! Currently only predictions of upto 3 months are allowed.");
            return;
        }
        else
        {
            setSelectedDate(event.target.value);
            props.setDate(event.target.value.split("-").reverse().join("-"));  //Convert YYYY-MM-DD to DD-MM-YYYY format 
        }
    }

    /*function dateYearBeforeDate()
    {
        var d = new Date(getDateFromProp()),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear()-1;
    
        if (month.length < 2) 
            month = '0' + month;
        if (day.length < 2) 
            day = '0' + day;
    
        return [year, month, day].join('-');
    }*/

    function dateYearAfterDate()
    {
        var d = new Date(getDateFromProp()),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear()+1;
    
        if (month.length < 2) 
            month = '0' + month;
        if (day.length < 2) 
            day = '0' + day;
    
        return [year, month, day].join('-');
    }

    return (
        <>
            <div className={props.className}>
            <label htmlFor="start">Showing Data for: </label>
            <input type="date" id="start" name="trip-start"
                value={selectedDate}
                    onChange={handleChange}
                    min={MIN_DATE}
                    max={dateYearAfterDate()}
                ></input>
                <p id="mode">Mode: <span className={props.mode+"Mode"} >{props.mode}</span></p>
            </div>
        </>
    );
}

export default Datepicker; 