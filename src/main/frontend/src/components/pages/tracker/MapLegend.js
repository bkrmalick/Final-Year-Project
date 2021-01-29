import React, { useState, useEffect, useCallback} from 'react'

//styles
import './MapLegend.css';

/*
    Credits: https://tilemill-project.github.io/tilemill/docs/guides/advanced-legends/
*/

function MapLegend(props)
{
    const INCREMENT = 25; //amount by which the legend values increment
    const CLIENT_WINDOW_WIDTH_LIMIT = 1100; //pixels


    const [clientWidth, setCW] = useState(getCurrentClientWindowwSize());

    //populate the colors array uptil 100 based on the increment value
    let colorValues = [];
    for (let i = 0; i <= 100; i += INCREMENT)
        colorValues.push(i);
    
    const setClientWidthCurrentClientSize = useCallback(
        () =>
        {
            setCW(getCurrentClientWindowwSize())
        }
        , []
    );
    
    //one the first component load - add resize listener 
    useEffect(
        () =>
        {
            window.addEventListener("resize", setClientWidthCurrentClientSize);
        
            return () => window.removeEventListener("resize", setClientWidthCurrentClientSize);
        }
    );
    
    return (
        <div className="legend">
            <div className="legend-title">Danger {clientWidth<CLIENT_WINDOW_WIDTH_LIMIT?"%":"Percentage" }</div>
        <div className="legend-scale">
        <ul className="legend-labels">
        {
            colorValues.map(cv => <li key={cv}><span style={{ background: props.colorGenerator(true, cv) }}></span>{cv}%</li>)
        
        }
        </ul>
        </div>
        <div className="legend-source"> {clientWidth<CLIENT_WINDOW_WIDTH_LIMIT?"":"Data Source: "} <a target="_blank" rel="noopener noreferrer" href="https://data.london.gov.uk/dataset/coronavirus--covid-19--cases">{clientWidth<735? "GLA" :"Greater London Authority"}</a></div>
        </div>

    );
}

function getCurrentClientWindowwSize()
{
    return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
}

export default MapLegend;