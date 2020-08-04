import React, {useState} from 'react';


function Datepicker() {

    const [date,setDate] = useState(getTodayDate());


    function getTodayDate() 
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
    }

    function handleChange(event)
    {
        //todo: logic to handle min/max
        setDate(event.target.value);
    }

    function dateYearBeforeNow()
    {
        var d = new Date(),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear()-1;
    
        if (month.length < 2) 
            month = '0' + month;
        if (day.length < 2) 
            day = '0' + day;
    
        return [year, month, day].join('-');
    }

    function dateYearAfterNow()
    {
        var d = new Date(),
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
            <div className="date">
            <label htmlFor="start">Date: </label>
            <input type="date" id="start" name="trip-start"
                value={date}
                onChange={handleChange}
                min={dateYearBeforeNow()}
                max={dateYearAfterNow()}
                ></input>
            </div>
        </>
    );
}

export default Datepicker; 