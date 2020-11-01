import React from 'react'
import FAQ from './FAQ'


//styles
import './About.css'
import './Avatar.scss'

function About()
{

    return <div id="about-page">
        <h1 className="center">About</h1><br/>
        <div className="center">
            
            
            <div className="avatar">
            <div className="avatar-img">
                <img alt="bkr-avatar" src="images/bkr1.jpg" />
            </div><br />
            <h2>Developer</h2>
            <p>Abu Bakar Naseer</p>
            </div>
                
            {/*<span className="down-arrow">&#8595;</span>*/}

            <div className="avatar">
            <div className="avatar-img">
                <img alt="fuhs-avatar" src="images/fuhs.jpg" />
            </div><br />
            <h3>Supervisor</h3>
            <p>Carsten Fuhs</p>
            </div>
            </div>
            <div className="FAQ-container">
                <FAQ />
            </div>
            
    </div>;
}


export default About;