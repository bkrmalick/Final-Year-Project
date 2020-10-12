import React, { useEffect, useState } from 'react'
import { useTransition, animated } from 'react-spring'
import QuestionType from './question/QuestionType';
import Question from './question/Question'

//styles
import './HelpUsCollect.css'

function HelpUsCollect(props)
{
    const [currentQuestionIndex, setCQI] = useState(0);
    const [answersMap, setAnswersMap] = useState(new Map()); // <questionID,ans>

    //TODO fetch from db
    const QUESTIONS = [
        { type: 3, text: "Hello, there.", timeout: 1000 },
        { type: 3, text:"We'll ask you a few questions regarding your health.", timeout: 2500 },
        { type: 2, text: "Firstly, lets get the legalities out of the way.\nThis will require us to store some data on you.", trueText:"Sure! Lets Go", falseText:"No thanks", exitOnFalse: true   },
        { type: 2, text: "Have you ever been tested positively for a COVID-19 test?", trueText:"Yes, I have", falseText:"Nope"   },
        { type: 2, text: "Has you or anyone in your household had the following syptoms in the past two weeks: \n🔹a high temperature\n🔹a new, continuous cough\n🔹a loss or change to your sense of smell or taste ",trueText:"Yes",falseText:"No"},
        { type: 1, text: "Where do you live?" },
        { type: 3, text: "That's all for now!\nThank you for choosing to help us", timeout: 3000 }
    ];

    const [questionsOnDisplay, setQOD] = useState([QUESTIONS[currentQuestionIndex]]);


    const transitions = useTransition(questionsOnDisplay, q => q.text, {
        from: { transform: 'translate3d(0,500px,0)' },
        enter: { transform: 'translate3d(0,0px,0)' },
        leave: { transform: 'translate3d(0,-500px,0)' },
        onDestroyed: toggleNextQuestion
    })

    function toggleNextQuestion()
    {
        //add the next question into the display array
        setQOD(questionsOnDisplay.concat([QUESTIONS[currentQuestionIndex]]));
    }

    function removeQuestionFromDisplay(question, ans)
    {
        console.log("remove: "+question.text+"\n "+ans)
        if (question.type !== QuestionType.STATEMENT)
        {
            //store answer in state 
            storeAnswer(question, ans)
        }

        //check if need to exit
        if (question.exitOnFalse && ans === question.falseText)
        {
            //change question to last question (prompt saying thank you)
            setCQI(QUESTIONS.length-1);
            //remove current question from display array
            setQOD(questionsOnDisplay.slice(QUESTIONS.length-1, questionsOnDisplay.length));
        }
        else
        {
            //increment pointer
            setCQI(currentQuestionIndex + 1);
            //remove question from display array
            setQOD(questionsOnDisplay.slice(currentQuestionIndex + 1, questionsOnDisplay.length));
        }
    }

    function storeAnswer(question, answer)
    {
        const answersMap_copy = new Map(answersMap);
        answersMap_copy.set(question.id+":"+question.text, answer);
        setAnswersMap(answersMap_copy);
    }


    //automatically trigger next question if current one is a statement
    useEffect(() =>
    {
        if (currentQuestionIndex === QUESTIONS.length - 1)
        {
            console.log(answersMap);
        }

        if (QUESTIONS[currentQuestionIndex].type === QuestionType.STATEMENT
            && currentQuestionIndex !== QUESTIONS.length - 1
        )
        {
            setTimeout(() => { removeQuestionFromDisplay(QUESTIONS[currentQuestionIndex]); }, QUESTIONS[currentQuestionIndex].timeout);
        }
        // eslint-disable-next-line
    }, [currentQuestionIndex])

    return transitions.map(({ item, props, key }, index) =>
    {
        return <div key={index} id='questions-container' >
            <animated.div key={index} style={props}>
                {<Question index={index} question={item} doneHandler={removeQuestionFromDisplay} />}
            </animated.div>
        </div>;
    })
}

export default HelpUsCollect;