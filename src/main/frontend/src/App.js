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

  const CONSOLE_LOGGING=true; //change for proeuction as affects performance

  if (!CONSOLE_LOGGING)
  {
    console.log = () => {};
  }

  return (
    <Router>
      <div className="App">
        <h3>logo</h3>
        <Nav />
        <Switch>
          <Route path="/" exact component={Home}/>
          <Route path="/about" exact component={About}/>
        </Switch>
      </div>
    </Router>
  );
}

export default App;
