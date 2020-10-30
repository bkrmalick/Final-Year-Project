//other
import React from 'react';
import './App.css';
import './Popup.css';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Popup from 'react-popup';

//navigation
import Navbar from './components/pages/nav/Navbar';

//pages
import Home from './components/pages/home/Home';
import Tracker from './components/pages/tracker/Tracker';
import NoMatch from './components/pages/nomatch/NoMatch';
import HelpUsCollect from './components/pages/help-us-collect/HelpUsCollect';
import Footer from './components/pages/footer/Footer';

function App()
{

  if (process.env.NODE_ENV !== "development") //don't console log in production as affects performance
  {
    console.log = () => { };
  }

  return (
    <Router>
      <Popup />
      <div className="App">
        <Navbar />
        <Switch>
          <Route path="/" exact component={Home} />
          <Route path="/go" exact component={Tracker} />
          <Route path="/help-us-collect" exact component={HelpUsCollect} />
          <Route component={NoMatch} />
        </Switch>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
