import React,{useState} from 'react'
import { Button } from '../../Button'
import './PostCodeForm.css'

//utils
import {getBoroughForPostCode} from '../../../utils/APIUtils'

function PostCodeForm(props)
{
    const [postCode,setPostCode] = useState("");
    const [errorText,setErrorText] = useState("");
    const [submitDisabled,setSubmitDisabled] = useState(false);

    function onSubmit(event)
    {
        event.preventDefault();

        setSubmitDisabled(true);

        if (postCode.trim().length > 0)
        {
            getBoroughForPostCode(postCode)
                .then(processPostCode)
                .catch(handleError);
        }     
        else
        {
            setErrorText("Please enter a Post Code");
        }
    }
    
    function processPostCode(resp)
    {
        console.log(resp);
        const BOROUGH=resp.data.borough;
        
        props.setSelectedLocationName(BOROUGH);
        setSubmitDisabled(false);
    }

    function handleError(err)
    {
        console.log(err.response);

        if(err.response.status===500)
        {
            //something went wrong with our backend
            setErrorText("Server error, please contact admin."); 
        }
        else
        {
            //something wrong with the user request
            const errorResp=JSON.parse(err.response.data.message);
            setErrorText(errorResp.error);
        }

        setSubmitDisabled(false);
    }

    function changeHandler(event)
    {
        setErrorText("");
        props.unselectSelectedLocation();
        //TODO super.casesDataMode = null;
        setPostCode(event.target.value);
    }
//<input type="submit" value="Check" disabled={submitDisabled} />
    return (
        <form id="input" onSubmit={onSubmit}>
          <label htmlFor="fname" id="LabelForPostCode">Post Code</label>
          <input type="text" id="postcode" value={postCode} onChange={changeHandler} /><br /><br />
          <p className="error">{errorText}</p>
            <Button type="submit" disabled={submitDisabled} buttonColor="blue" buttonSize="btn--medium">GO</Button>
        </form>
    ); 
}

export default PostCodeForm;