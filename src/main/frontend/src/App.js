//other
import React from 'react';
import './App.css';
import {BrowserRouter as Router, Switch, Route} from 'react-router-dom';

//navigation
import Navbar from './components/pages/Navbar'

//pages
import Home from './components/pages/home/Home'
import Tracker from './components/pages/Tracker'

function App() {
  
  if (process.env.NODE_ENV!=="development") //don't console log in production as affects performance
  {
    console.log = () => {};
  }

  return (
    <Router>
      <div className="App">
        <Navbar />
        <Switch>
          <Route path="/" exact component={Home}/>
          <Route path="/go" exact component={Tracker}/>
        </Switch>
      </div>
    </Router>
  );
}

export default App;
