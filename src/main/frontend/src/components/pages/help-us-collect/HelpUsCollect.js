import React, {useEffect, useState } from 'react'
import QuestionsContainer from './question/QuestionsContainer';
import { getFormQuestions } from '../../../utils/APIUtils';
import ClipLoader from "react-spinners/ClipLoader";
import Popup from 'react-popup';

//styles
import './HelpUsCollect.css'

function HelpUsCollect()
{
    const [questionsFromAPI, setQFAPI] = useState(undefined);
    const [errorFlag, setEF] = useState(false);
    const questionsLoaded = (questionsFromAPI !== undefined);
    let toReturn;

       /* //TODO fetch from db
    const questions=[
        { type: 3, text: "Hello, there.", timeout: 1000 },
        { type: 3, text:"We'll ask you a few questions regarding your health.", timeout: 2500 },
        { type: 2, text: "Firstly, lets get the legalities out of the way.\nThis will require us to store some data on you.", trueText:"Sure! Lets Go", falseText:"No thanks", exitOnFalse: true   },
        { type: 2, text: "Have you ever been tested positively for a COVID-19 test?", trueText:"Yes, I have", falseText:"Nope"   },
        { type: 2, text: "Has you or anyone in your household had the following syptoms in the past two weeks: \n🔹a high temperature\n🔹a new, continuous cough\n🔹a loss or change to your sense of smell or taste ",trueText:"Yes",falseText:"No"},
        { type: 1, text: "Where do you live?" },
        { type: 3, text: "That's all for now!\nThank you for choosing to help us", timeout: 3000 }
    ];
    */

    useEffect(() =>
    {
        getFormQuestions()
            .then(
                res =>
                {
                    const sortedQuestions = res.data.questions.sort((a, b) => a.id - b.id); //sorted acc. to ID
                    setQFAPI(sortedQuestions);
                    console.log(sortedQuestions);
                }
            )
            .catch(err =>
            {
                console.log(err.response);
                Popup.alert("Sorry, there was an error while fetching the questions from the server. Please contact admin.");
                setEF(true);
            });
        
    }, [])

    

    if (errorFlag===true)
        toReturn =<div className="HelpUsCollect-container"><span role="img" aria-label="error">❌</span></div>;
    else if (!questionsLoaded)
        toReturn = <div className="HelpUsCollect-container"><ClipLoader /></div>;
    else
        toReturn = <QuestionsContainer questions={questionsFromAPI} />;
    
    return toReturn; 
}

export default HelpUsCollect;