import React from 'react'
import FAQ from './faq/FAQ'
import Avatar from './avatar/Avatar'

//styles
import './About.css'

function About()
{

    return <div id="about-page">
        <h1 className="center title">About</h1><br />
        <div className="center">
            <Avatar imgAlt="bkr-avatar" imgSrc="images/bkr1.jpg" title="Developer" name="Abu Bakar Naseer" />
            <Avatar imgAlt="fuhs-avatar" imgSrc="images/fuhs.jpg" title="Supervisor" name="Carsten Fuhs" />
        </div>

        <div className="FAQ-container">
            <FAQ />
        </div>
    </div>;
}


export default About;