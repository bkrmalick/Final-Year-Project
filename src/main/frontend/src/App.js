//other
import React from 'react';
import './App.css';
import {BrowserRouter as Router, Switch, Route} from 'react-router-dom';

//navigation
import Navbar from './components/pages/Navbar'

//pages
import Home from './components/pages/Home'
import About from './components/pages/About'

function App() {
  
  if (process.env.NODE_ENV!=="development") //change for production as affects performance
  {
    console.log = () => {};
  }

  return (
    <Router>
      <div className="App">
        <Navbar />
        <Switch>
          <Route path="/" exact component={Home}/>
          <Route path="/about" exact component={About}/>
        </Switch>
      </div>
    </Router>
  );
}

export default App;
