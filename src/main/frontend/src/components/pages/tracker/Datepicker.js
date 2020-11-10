import React, {useState} from 'react';
import Popup from 'react-popup';

//styles
import './Datepicker.css'

function Datepicker(props)
{
    const [selectedDate, setSelectedDate] = useState("");
    const MAX_MONTHS_PREDICTION = 3;
    const MIN_DATE = "2020-02-01"; //min time for which we have data

    /*
    update the date state variable acc. to prop ONLY if it already doesn't have a user selected value    
    (once a new date is selected, prop date will match it)
    */
    if(selectedDate!==getDateFromProp())
        setSelectedDate(getDateFromProp());

    function getDateFromProp() 
    {
        const d=props.date;

        if(d===undefined || d===null) 
            return "";

        //Convert DD-MM-YYYY to YYYY-MM-DD format 
       return reverseDate(d);
    }

    //Converts DD-MM-YYYY to YYYY-MM-DD format or vice versa
    function reverseDate(date)
    {
        if (date===undefined || date === null)
            return "";

        return date.split("-").reverse().join("-");
    }

    function handleChange(event)
    {
        let newDate = new Date(event.target.value);   
        let maxDate = new Date(reverseDate(props.casesDataRefreshDate));
        maxDate.setMonth(maxDate.getMonth() + MAX_MONTHS_PREDICTION);
       
        //don't update if the same value
        if (newDate === selectedDate)
            return;
        
        //don't accept inputs outside bounds (min date handled by parent component using backend message)
        if(newDate>maxDate)
        {
            Popup.alert(`Sorry! Currently only predictions of upto ${MAX_MONTHS_PREDICTION} months are allowed. 
            
            Please select a date before ${maxDate.toLocaleDateString("en-GB")}`);
            return;
        }
        else
        {
            setSelectedDate(event.target.value);
            props.setDate(reverseDate(event.target.value));
        }
    }

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
    /*
    function addMonthsToDate(months, date)
    {

        let tmpDate = new Date(date);
        let newDate = new Date(tmpDate.setMonth(tmpDate.getMonth()+months));

       
        return formatDate(newDate);
    }
    */

    return (
        <>
            <div className={props.className}>
            <label htmlFor="start">Showing Data for: </label>
                <input type="date" id="start"
                    value={selectedDate}
                    onChange={handleChange}
                    min={MIN_DATE}
                    max={dateYearAfterDate()}//addMonthsToDate(MAX_MONTHS_PREDICTION, reverseDate(props.casesDataRefreshDate))}
                ></input>
                <p id="mode">Mode: <span className={props.mode+"Mode"} >{props.mode}</span></p>
            </div>
        </>
    );
}

export default Datepicker; 