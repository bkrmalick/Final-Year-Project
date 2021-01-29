import React from 'react'
import { FaQuestionCircle } from 'react-icons/fa'

//styles
import './LegendToolTip.css'

/* TOOLTIP Credits: http://www.menucool.com/tooltip/css-tooltip */

function LegendToolTip(props)
{
    return (
        <div className="tooltip">
        <FaQuestionCircle/>
        <div className="right">
            <div className="text-content">
                <p>A relative danger level of the area. <br/><br/> Based on the following:</p><br />
                <ul>
                    <li>Infection counts in the past two weeks</li>
                    <li>Population density</li>
                </ul>
            </div>
            <i></i>
        </div>
    </div>
    );
}

export default LegendToolTip;