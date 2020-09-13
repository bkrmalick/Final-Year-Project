import React, {useState} from 'react';
import './Datepicker.css'

function Datepicker(props)
{
    const [date,setDate] = useState("");
    //const [mode, setMode] = useState("Normal");

    //update the date state variable acc. to prop ONLY if it already doesn't have a user selected value
    if(date==="" && date!==getDateFromProp())
        setDate(getDateFromProp());

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
        let newDate =new Date(event.target.value);

        let maxDate= new Date(dateYearAfterDate());
        let minDate= new Date(dateYearBeforeDate());
        //let refreshDate= new Date(getDateFromProp());
   
        //don't accept inputs outside bounds
        if(newDate>maxDate || newDate<minDate)
            return;
        else
        {
           /* if(newDate>refreshDate)
                setMode("Prediction");
            else
                setMode("Normal");*/

            setDate(event.target.value);
            props.setDate(event.target.value.split("-").reverse().join("-"));  //Convert YYYY-MM-DD to DD-MM-YYYY format 
        }
    }

    function dateYearBeforeDate()
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

    return (
        <>
            <div className={props.className}>
            <label htmlFor="start">Showing Data for: </label>
            <input type="date" id="start" name="trip-start"
                value={date}
                onChange={handleChange}
                min={dateYearBeforeDate()}
                max={dateYearAfterDate()}
                ></input>
                <p id="mode">Mode: <span className={props.mode+"Mode"} >{props.mode}</span></p>
            </div>
        </>
    );
}

export default Datepicker; 