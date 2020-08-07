import React,{useState} from 'react';

import './InputForm.css';

//utils
import {getBoroughForPostCode} from '../utils/APIUtils'

function InputForm()
{
    const [postCode,setPostCode] = useState("");
    const [errorText,setErrorText] = useState("");
    const [submitDisabled,setSubmitDisabled] = useState(false);

    function submit(event)
    {
        event.preventDefault();

        setSubmitDisabled(true);

        getBoroughForPostCode(postCode)
            .then(processPostCode)
            .catch(handleError);
    }
    
    function processPostCode(resp)
    {
        console.log(resp);
        const BOROUGH=resp.data.borough;
        console.log(BOROUGH);

        setSubmitDisabled(false);
    }

    function handleError(err)
    {
        console.log(err.response);

        if(err.response.status===500)
        {
            setErrorText("Server error, please contact admin.");
        }
        else
        {
            const errorResp=JSON.parse(err.response.data.message);
            setErrorText(errorResp.error);
        }

        setSubmitDisabled(false);
    }

    function changeHandler(event)
    {
        setErrorText("");
        setPostCode(event.target.value);
    }

    return (
        <form id="input" onSubmit={submit}>
          <label htmlFor="fname" id="LabelForPostCode">Post Code</label>
          <input type="text" id="postcode" value={postCode} onChange={changeHandler} /><br /><br />
          <p className="error">{errorText}</p>
          <input type="submit" value="Check"  disabled={submitDisabled}/>
        </form>
    ); 
}

export default InputForm;