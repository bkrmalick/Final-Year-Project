import React, { useEffect, useState } from 'react'
import { useTransition, animated } from 'react-spring'
import QuestionType from './QuestionType'
import Question from './Question'
import { postFormAnswers} from '../../../../utils/APIUtils';
import Popup from 'react-popup';

//styles
import './QuestionsContainer.css'


function QuestionsContainer(props)
{

    const [currentQuestionIndex, setCQI] = useState(0);
    const [answersMap, setAnswersMap] = useState(new Map()); // <questionID,ans>

    const QUESTIONS = props.questions

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
        answersMap_copy.set(question.id+": "+question.text, answer);
        setAnswersMap(answersMap_copy);
    }


    
    useEffect(() =>
    {
        //if last questions - post answers to server
        if (currentQuestionIndex === QUESTIONS.length - 1) 
        {
            console.log(answersMap);
            console.log("Trying to post...");
            postFormAnswers(answersMap).catch(err =>
            {
                console.log(err.response);
                Popup.alert("Sorry, there was an error while submitting the answers to the server. Please try again later.");
            });
        }

        //automatically trigger next question if current one is a statement
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

export default QuestionsContainer;