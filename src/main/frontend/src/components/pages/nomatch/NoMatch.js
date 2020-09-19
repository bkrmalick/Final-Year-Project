import React, { useEffect } from 'react';
import Popup from 'react-popup';
import {Redirect} from 'react-router-dom';


function NoMatch(props)
{
    useEffect(() => {
        Popup.alert("No page found for that path! Redirecting you to the home page.");
    });
    
    return <Redirect to="/" />;
}

export default NoMatch;
