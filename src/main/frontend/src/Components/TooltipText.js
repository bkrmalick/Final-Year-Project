import React from 'react';

function App(props) {
    return (
        <>
		<p>{"LOCATION: "+props.location}</p>
		<p>{"DANGER:"+String(props.dangerLevel)}</p>
        <p>{"Cases in past two wks: "+String(props.casesInPastTwoWks)}</p>
		</>
    );
  }
  
  export default App;