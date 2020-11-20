import React, { useEffect, useState } from 'react'
import TooltipHeatMap from './map/TooltipHeatMap'
import Popup from 'react-popup'
import { Redirect } from 'react-router-dom'
import { binaryPopup } from '../../../utils/PopupUtils'

function Tracker() 
{
  const POPUP_DELAY_IN_MS = 180000; //3*60*1000
  const [redirectFlag, setRF] = useState(false);

  //set timeout for the intial popup
  useEffect(() =>
  {
    let mounted = true;

    setTimeout(() =>
    {
      if (mounted)
      {
        binaryPopup('Enjoying the app?',
          'Help expand the data by answering a couple of questions.',
          'Back', 'Sure!', undefined, () => { Popup.close(); setRF(true); }
        );
      }
    }
      , POPUP_DELAY_IN_MS)
    
    return () => { mounted = false };
  }, []);

  return (redirectFlag ? <Redirect to="/help-us-collect" /> : <TooltipHeatMap />);
}

export default Tracker;
