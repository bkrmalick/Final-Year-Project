import React, {useState} from 'react'
import QuestionType from './QuestionType';

function Question(props)
{ 
    let toReturn; 
    const [ans, setAns] = useState(" ");
    

    switch (props.question.type)
    {
        case QuestionType.TEXT:
            toReturn = (<>
                <label onSubmit={props.question.doneHandler} htmlFor="question" className="question-label">{props.question.text}</label><br />
                <input type="text" id="question" onChange={(e) => { setAns(e.target.value) }} value={props.question.defaulttext} /><br />
                <input type="submit" value="⏩" style={ ans.trim().length === 0  ? { display: "none" } : {}}/> 
                <br />
            </>);
            break;
        case QuestionType.STATEMENT:
            toReturn = (<>
                <label className="question-label">{props.question.text}</label><br />
            </>);
            break;
        case QuestionType.BINARY:
            toReturn = (<>
                <label className="question-label">{props.question.text}</label><br />
                <label className="question-label">{props.question.secondText}</label><br />
                <input type="submit" value={props.question.trueText} onClick={e=>setAns(props.question.trueText)}/>
                <input type="submit" value={props.question.falseText} onClick={e=>setAns(props.question.falseText)}/>
            </>);
            break;
        default:
            toReturn = (<></>);
    }
    
    function onSubmit(e)
    {
        e.preventDefault();
        props.doneHandler(props.question,ans);
    }
      

    return (
        <form className="question-element" onSubmit={onSubmit} >
            {toReturn}
        </form>
    );
}



export default Question;