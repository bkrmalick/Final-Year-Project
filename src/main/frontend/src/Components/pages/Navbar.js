import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Button } from './Button';

import { FcMindMap } from 'react-icons/fc'
import { FaBars, FaTimes } from 'react-icons/fa';

import './Navbar.css';

function Nav() {
  const [click, setClick] = useState(false);
  const [button, setButton] = useState(true);

  function handleClick() {
    setClick(!click);
  }

  function closeMobileMenu() {
    setClick(false);
  }

  function showButton() {
    if (window.innerWidth <= 960) {
      setButton(false);
    }
    else {
      setButton(true);
    }
  }

  useEffect(() => {showButton();}, []);

  window.addEventListener('resize', showButton);

  return (
    <>
      <div className="navbar" >
        <div className="navbar-container container">
          <Link to="/" className="navbar-logo">
            <FcMindMap className="navbar-icon" />
            COVIDTracker
          </Link>

          <div className='menu-icon' onClick={handleClick}>
            {click ? <FaTimes /> : <FaBars />}
          </div>

          <ul className={click ? 'nav-menu active' : 'nav-menu'}>
            <li className="nav-item">
              <Link to="/" className="nav-links">Home</Link>
            </li>
            <li className="nav-item">
              <Link to="/about" className="nav-links">About</Link>
            </li>
            <li className='nav-btn'>
                {button ? (
                  <Link to='/' className='btn-link'>
                    <Button buttonStyle='btn--outline'>CONTACT</Button>
                  </Link>
                ) : (
                  <Link to='/' className='btn-link'>
                    <Button
                      buttonStyle='btn--outline'
                      buttonSize='btn--mobile'
                      onClick={closeMobileMenu}
                    >
                      CONTACT
                    </Button>
                  </Link>
                )}
              </li>
          </ul>
        </div>
      </div>
    </>
  );
}

export default Nav;

