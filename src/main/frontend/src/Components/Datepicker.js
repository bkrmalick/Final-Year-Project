import React, {useState} from 'react';


function Datepicker(props) {

    const [date,setDate] = useState("");
    const [mode, setMode] = useState("Normal");

    //update the date state variable acc. to prop ONLY if it already doesn't have a user selected value
    if(date==="" && date!==getDataRefreshDateFromProp())
        setDate(getDataRefreshDateFromProp());

    function getDataRefreshDateFromProp() 
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

        let maxDate= new Date(dateYearAfterRefreshDate());
        let minDate= new Date(dateYearBeforeRefreshDate());
        let refreshDate= new Date(getDataRefreshDateFromProp());
   
        //don't accept inputs outside bounds
        if(newDate>maxDate || newDate<minDate)
            return;
        else
        {
            if(newDate>refreshDate)
                //alert("You have selected a date beyond what we have data for. The application will now try to predict the data for the selected date.");
                setMode("Prediction");
            else
                setMode("Normal");

            setDate(event.target.value);
        }
    }

    function dateYearBeforeRefreshDate()
    {
        var d = new Date(getDataRefreshDateFromProp()),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear()-1;
    
        if (month.length < 2) 
            month = '0' + month;
        if (day.length < 2) 
            day = '0' + day;
    
        return [year, month, day].join('-');
    }

    function dateYearAfterRefreshDate()
    {
        var d = new Date(getDataRefreshDateFromProp()),
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
            <div className="dateBox">
            <label htmlFor="start">Showing Data from: </label>
            <input type="date" id="start" name="trip-start"
                value={date}
                onChange={handleChange}
                min={dateYearBeforeRefreshDate()}
                max={dateYearAfterRefreshDate()}
                ></input>
                <p id="mode">Analysis Mode: <span className={mode+"Mode"} >{mode}</span></p>
            </div>
        </>
    );
}

export default Datepicker; 