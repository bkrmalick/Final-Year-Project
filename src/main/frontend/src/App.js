//other
import React from 'react';
import './App.css';
import {BrowserRouter as Router, Switch, Route} from 'react-router-dom';

//navigation
import Nav from './Components/Nav'

//pages
import Home from './Components/Home'
import About from './Components/About'

function App() {
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
