import React, { useState } from 'react'
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
                <input type="text" id="question" onChange={(e) => { setAns(e.target.value) }} /><br />
                <input type="submit" value="⏩" style={ans.trim().length === 0 ? { display: "none" } : {}} />
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
                <input type="submit" value={props.question.trueText} onClick={e => setAns(props.question.trueText)} />
                <input type="submit" value={props.question.falseText} onClick={e => setAns(props.question.falseText)} />
            </>);
            break;
        case QuestionType.DDM:
            toReturn = (<>
                <label onSubmit={props.question.doneHandler} htmlFor="question" className="question-label">{props.question.text}</label><br />
                <select id="question" className="select-css" onChange={(e) => { setAns(e.target.value) }} onLoad={(e) => { setAns(e.target.value) }}>
                    <option selected="selected" value={"default"} >...</option>
                    {
                        sortAlphabetically(props.question.options)
                            .map((v, k) => <option key={k} value={v} >{v}</option>)
                    }
                </select><br />
                <input type="submit" value="⏩" style={ans.trim().length === 0 || !props.question.options.includes(ans) ? { display: "none" } : {}}/>
            </>);
            break;
        default:
            toReturn = (<></>);
    }

    function onSubmit(e)
    {
        e.preventDefault();
        props.doneHandler(props.question, ans);
    }

    function sortAlphabetically(arr)
    {
        return arr.sort((s1, s2) =>
        {
            if (s1.toUpperCase() > s2.toUpperCase())
                return 1;
            else if (s1.toUpperCase() < s2.toUpperCase())
                return -1;
            else
                return 0;
        });
    }


    return (
        <form className="question-element" onSubmit={onSubmit} >
            {toReturn}
        </form>
    );
}



export default Question;