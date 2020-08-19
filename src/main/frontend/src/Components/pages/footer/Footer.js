import React from 'react';
import './Footer.css';
import { Link } from 'react-router-dom';
import {FaGithub,FaLinkedin} from 'react-icons/fa';
import { FcMindMap } from 'react-icons/fc';

function Footer() {
  return (
    <div className='footer-container'>
  
      <div className='footer-links'>
        <div className='footer-link-wrapper'>
          <div className='footer-link-items'>
            <h2>Info</h2>
            <Link to='/'>About</Link>
          </div>
          <div className='footer-link-items'>
            <h2>Data</h2>
            <Link to='/'>Sources</Link>
            <Link to='/'>Help Us Collect</Link>
                  </div>
                  <div className='footer-link-items'>
            <h2>Contact</h2>
            <Link to='/'>Contact</Link>
            <a href='https://www.linkedin.com/in/bkrmalick/'
              target='_blank'
              rel="noopener noreferrer"
              aria-label='LinkedIn'>LinkedIn</a>
          </div>
        </div>
      </div>
      <section className='social-media'>
        <div className='social-media-wrap'>
          <div className='footer-logo'>
            <Link to='/' className='social-logo'>
            <FcMindMap className="navbar-icon" />
            COVIDTracker
            </Link>
          </div>
                  <small className='website-rights'>CT © {new Date().getFullYear()}</small>
          <div className='social-icons'>
            <a
              className='social-icon-link'
              href='https://www.linkedin.com/in/bkrmalick/'
              target='_blank'
              rel="noopener noreferrer"
              aria-label='LinkedIn'
            >
              <FaLinkedin />
            </a>
            <a
              className='social-icon-link'
              href='https://github.com/bkrmalick/Final-Year-Project'
              target='_blank'
              rel="noopener noreferrer"
              aria-label='Github'
            >
              <FaGithub />
            </a>
          </div>
        </div>
      </section>
    </div>
  );
}

export default Footer;