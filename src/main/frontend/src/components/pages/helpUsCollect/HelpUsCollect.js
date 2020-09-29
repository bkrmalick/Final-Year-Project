import React, { useState }  from 'react'
import {useTransition, animated} from 'react-spring'
import QuestionType from './question/QuestionType';
import Question from './question/Question'

//styles
import './HelpUsCollect.css'

function HelpUsCollectFunc(props) {

    const [currentQuestionIndex, setCQI] = useState(0);

    const QUESTIONS = [
        { type: QuestionType.STATEMENT, text: "Hello.", timeout:1000 },
        { type: QuestionType.TEXT, text: "What is your name?" },
        { type: QuestionType.TEXT, text: "Where do you live?" },
        { type: QuestionType.STATEMENT, text: "That's all for now!", secondText:"Thank you for choosing to help us", timeout:3000  }
    ];

    const [questionsOnDisplay, setQOD] = useState([QUESTIONS[currentQuestionIndex]]);
        /*
        { type: QuestionType.STATEMENT, text: "Hello.", delayText: "Thank you for choosing to help us" },
        { type: QuestionType.STATEMENT, text: "That's all for now!" },]);*/
    
    
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

    function removeQuestionFromDisplay()
    {
        //increment pointer
        setCQI(currentQuestionIndex + 1);
        //remove question from display array
        setQOD(questionsOnDisplay.slice(currentQuestionIndex + 1, questionsOnDisplay.length));
    }

    if (QUESTIONS[currentQuestionIndex].type === QuestionType.STATEMENT)
    {
        setTimeout(removeQuestionFromDisplay, QUESTIONS[currentQuestionIndex].timeout);
    }

    return transitions.map(({ item, props, key }) => {
        return <div  key={key}  id='questions-container' >
            <animated.div  key={key} style={props}>
                {<Question
                    type={item.type}
                    text={item.text}
                    key={key}
                    styles={props}
                    secondText={item.secondText}
                    doneHandler={removeQuestionFromDisplay}
                />}
            </animated.div>
        </div>;
    })
}

export default HelpUsCollectFunc;