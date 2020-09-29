import React, { useState } from 'react'
import { useTransition, animated } from 'react-spring'
import QuestionType from './question/QuestionType';
import Question from './question/Question'

//styles
import './HelpUsCollect.css'

function HelpUsCollectFunc(props) {

    const [currentQuestionIndex, setCQI] = useState(0);
    const [answersMap, setAnswersMap] = useState(new Map()); // <questionID,ans>

    //TODO fetch from db
    const QUESTIONS = [
        { id: 1, type: QuestionType.STATEMENT, text: "Hello.", timeout: 2000 },
        { id: 2, type: QuestionType.TEXT, text: "What is your name?" },
        { id: 3, type: QuestionType.TEXT, text: "Where do you live?" },
        { id: 4, type: QuestionType.STATEMENT, text: "That's all for now!", secondText: "Thank you for choosing to help us", timeout: 3000 }
    ];

    const [questionsOnDisplay, setQOD] = useState([QUESTIONS[currentQuestionIndex]]);


    const transitions = useTransition(questionsOnDisplay, q => q.text, {
        from: { transform: 'translate3d(0,500px,0)' },
        enter: { transform: 'translate3d(0,0px,0)' },
        leave: { transform: 'translate3d(0,-500px,0)' },
        onDestroyed: toggleNextQuestion
    })

    function toggleNextQuestion() {
        //add the next question into the display array
        setQOD(questionsOnDisplay.concat([QUESTIONS[currentQuestionIndex]]));
    }

    function removeQuestionFromDisplay(question, ans)
    {
        if (question.type !== QuestionType.STATEMENT) {
            //store answer in state 
            storeAnswer(question, ans)
        }

        //increment pointer
        setCQI(currentQuestionIndex + 1);
        //remove question from display array
        setQOD(questionsOnDisplay.slice(currentQuestionIndex + 1, questionsOnDisplay.length));
    }

    function storeAnswer(question, answer)
    {
        const answersMap_copy = answersMap;
        answersMap_copy.set(question.id, answer);
        setAnswersMap(answersMap_copy);
    }


    if (QUESTIONS[currentQuestionIndex].type === QuestionType.STATEMENT && currentQuestionIndex !== QUESTIONS.length - 1) {
        setTimeout(()=>removeQuestionFromDisplay(QUESTIONS[currentQuestionIndex]), QUESTIONS[currentQuestionIndex].timeout);
    }
            
    if (currentQuestionIndex === QUESTIONS.length - 1) {
        console.log(answersMap);
    }

    return transitions.map(({ item, props, key }) => {
        return <div key={key} id='questions-container' >
            <animated.div key={key} style={props}>
                {<Question question={item} doneHandler={removeQuestionFromDisplay}/>}
            </animated.div>
        </div>;
    })
}

export default HelpUsCollectFunc;