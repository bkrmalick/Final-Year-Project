import React from 'react';
import { Link } from 'react-router-dom';
import { Button } from '../../../Button';

import { FaInfoCircle } from 'react-icons/fa';

import './VideoHeroSection.css';

function VideoHeroSection() {
    //NEED TO GIVE CREDS TO: <a rel="nofollow" href="http://www.videezy.com/">B Roll by Videezy!</a>

    //https://www.videezy.com/abstract/18793-virus-cells-viruses-virus-cells-under-microscope-floating-in-fluid-with-blue-background

    //TODO add parallax effect
    return (
        <div className="hero-container">
            <video src="videos/video-2.mp4" autoPlay loop muted />
            <h1 className="fade-in">STAY INFORMED, STAY SAFE</h1> 
            <p className="fade-in">Track recent COVID-19 cases in London</p>
            <div className="hero-btns">
                <Link to="/go" >
                        <Button className="btns" buttonStyle="btn--outline" buttonSize="btn--large">GO</Button>
                </Link>

                <Link to="/" >
                    <Button className="btns" buttonStyle="btn--primary" buttonSize="btn--large"><FaInfoCircle className="navbar-icon" /> MORE INFO</Button>
                </Link>
            </div>
        </div>
    );
}

export default VideoHeroSection
