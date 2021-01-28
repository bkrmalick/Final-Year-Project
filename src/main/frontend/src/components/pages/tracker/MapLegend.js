import React from 'react'

//styles
import './MapLegend.css';

function MapLegend()
{
    
    return (
        <div className="map-legend">
            <div className="map-legend-item"><i style={{ background: 'black' }}></i>1</div>
            <br />
            <div className="map-legend-item"><i style={{ background: 'black' }}></i>2</div>
            <br />
            <div className="map-legend-item"><i style={{ background: 'black' }}></i>3</div>			
            <br />
            <div className="map-legend-item"><i style={{ background: 'black' }}></i>4</div>			
        </div>	
    );
}

export default MapLegend;