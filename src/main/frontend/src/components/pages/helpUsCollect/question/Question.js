import React from 'react'
import QuestionType from './QuestionType';

function Question(props)
{ 
    
    let toReturn; 

    if (props.type === QuestionType.TEXT)
    {
        toReturn = (<>
            <label onSubmit={props.doneHandler} htmlFor="question" className="question-label">{props.text}</label><br/>
            <input type="text" id="question" value={props.defaulttext===undefined?"...":props.defaulttext} /><br />
            <input type="submit" value="OK ✅" />
            <br />
            </> );
    }
    else if (props.type === QuestionType.STATEMENT)
    {
        toReturn = (<>
                <label className="question-label">{props.text}</label><br />
                <label className="question-label">{props.delayText}</label><br />
        </>);

        /*toReturn = (<>
            <Typist avgTypingDelay={80} cursor={{ hideWhenDone: true }} onTypingDone={props.doneHandler}>
                <Typist.Delay ms={500}/>
                <label className="question-label">{props.text}</label>
                <Typist.Backspace count={props.text.length} delay={500} />
                <label className="question-label">{props.delayText}</label><br />
                <Typist.Backspace count={props.text.length} delay={500} />
            </Typist>
            </> );*/
    }

    return (
        <form className="question-form">
            {toReturn}
        </form>
    );
}



export default Question;