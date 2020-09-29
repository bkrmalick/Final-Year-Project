import React, {useState} from 'react'
import QuestionType from './QuestionType';

function Question(props)
{ 
    let toReturn; 
    const [ans, setAns] = useState("");

    switch (props.type)
    {
        case QuestionType.TEXT:
            toReturn = (<>
                <label onSubmit={props.doneHandler} htmlFor="question" className="question-label">{props.text}</label><br />
                <input type="text" id="question" onChange={(e) => { setAns(e.target.value) }} value={props.defaulttext} /><br />
                <input type="submit" value="OK ✅" />
                <br />
            </>);
            break;
        case QuestionType.STATEMENT:
            toReturn = (<>
                <label className="question-label">{props.text}</label><br />
                <label className="question-label">{props.secondText}</label><br />
            </>);
            break;
        default:
            toReturn = (<></>);
    }
    
    function onSubmit(e)
    {
        e.preventDefault();
        console.log(ans);
        props.doneHandler();
    }
      

    return (
        <form className="question-element" key={props.text} onSubmit={onSubmit}>
            {toReturn}
        </form>
    );
}



export default Question;