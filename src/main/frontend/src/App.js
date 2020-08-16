//other
import React from 'react';
import './App.css';
import {BrowserRouter as Router, Switch, Route} from 'react-router-dom';

//navigation
import Nav from './components/Nav'

//pages
import Home from './components/Home'
import About from './components/About'

function App() {
  
  if (process.env.NODE_ENV!=="development") //change for production as affects performance
  {
    console.log = () => {};
  }

  return (
    <Router>
      <div className="App">
        <Nav />
        <h3>logo</h3>
        <Switch>
          <Route path="/" exact component={Home}/>
          <Route path="/about" exact component={About}/>
        </Switch>
      </div>
    </Router>
  );
}

export default App;
