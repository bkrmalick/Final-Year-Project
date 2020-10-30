import React from 'react';

function App(props) {
    return (
        <>
		<h1>{props.location}</h1><br/>
		<p>{String(props.dangerLevel)+"%"}</p> <br/>
        <p className="dangerPercentage">{"cases in past two wks: "+String(props.casesInPastTwoWks)}</p>
		</>
    );
  }
  
  export default App;