import React from 'react'
import Question from './question/Question'
import QuestionType from './question/QuestionType';

//styles
import './HelpUsCollect.css'

class HelpUsCollect extends React.Component {
    constructor(props) {
        super(props);
        this.toggleDone = this.toggleDone.bind(this);

        this.state = {
            quesAnsMap: new Map(),
            currentQuestionIndex: 0,
            QUESTIONS: Object.freeze([
                <Question type={QuestionType.STATEMENT} text="Hello." delayText="Thank you for choosing to help us" doneHandler={this.toggleDone} />,
                <Question type={QuestionType.TEXT} text="What is your name?" doneHandler={this.toggleDone} />,
                <Question type={QuestionType.STATEMENT} text="That's all for now!" doneHandler={this.toggleDone} />,
            ])

        };
    }
    componentDidMount()
    {
        const { currentQuestionIndex, QUESTIONS } = this.state;

        console.log(QUESTIONS[currentQuestionIndex].props);
        console.log(QuestionType.TEXT);
        if (QUESTIONS[currentQuestionIndex].props.type === QuestionType.STATEMENT) {
            setInterval(() => {
                this.toggleDone();
            }, 2000);
        }
    }

    toggleDone() {
        if (this.state.currentQuestionIndex < this.state.QUESTIONS.length - 1) {
            this.setState({
                currentQuestionIndex: this.state.currentQuestionIndex + 1
            });
        }
    }

    render() {
        return <>
            <div id='questions-container'>
                {this.state.QUESTIONS[this.state.currentQuestionIndex]}
            </div>
        </>;
    }
}

export default HelpUsCollect;