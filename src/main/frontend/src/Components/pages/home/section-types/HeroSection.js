import React from 'react';
import { Link } from 'react-router-dom';
import { Button } from '../../../Button';

import { FaInfoCircle } from 'react-icons/fa';

import './HeroSection.css';

function HeroSection({lightBg, topLine, lightText,lightTextDesc, headLine,description, buttonLabel, img,alt,imgStart}) {
    //NEED TO GIVE CREDS TO: <a rel="nofollow" href="http://www.videezy.com/">B Roll by Videezy!</a>
    //DEFAULT: <video src="videos/video-2.mp4" autoPlay loop muted />

    //hero-container
    return (
        <div className={lightBg?"home__hero-section":"home__hero-section darkBg"}>
            <div className="container">
                <div className="row home__hero-row" style={{display:"flex", flexDirection:imgStart==="start"?"row-reverse":"row"}}>
                    <div className="col">
                        <div className="home__hero-text-wrapper">
                            <div className="top-line">{topLine}</div>
                            <h1 className={lightText ? "heading" : "heading dark"}>{headLine}</h1>
                            <p className={lightTextDesc ? "home__hero-subtitle" : "home__hero-subtitle dark"}>{description}</p>
                                {buttonLabel !== "" ?
                                <Link to="/go" >
                                    <Button buttonColor="blue" buttonSize="btn--wide">{buttonLabel}</Button>
                                </Link>
                                : ""}
                        </div>
                    </div>
                    <div className="col">
                        <div className="home__hero-img-wrapper">
                            <img src={img} alt={alt} className="home__hero-img"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default HeroSection
