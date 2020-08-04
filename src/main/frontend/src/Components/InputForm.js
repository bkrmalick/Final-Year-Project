import React from 'react';

import './InputForm.css';

function inputForm()
{

    function submit(event)
    {
        event.preventDefault();
        console.log("submit");
    }

    return (
        <form id="input" onSubmit={submit}>
          <label htmlFor="fname" id="LabelForPostCode">Post Code</label>
          <input type="text" id="postcode" /><br /><br />
          <input type="submit" value="Check" />
        </form>
    ); 
}

export default inputForm;