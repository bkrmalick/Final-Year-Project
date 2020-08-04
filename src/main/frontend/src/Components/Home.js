import React from 'react';
import TooltipHeatMap from './TooltipHeatMap';
import InputForm from './InputForm';
import DatePicker from './Datepicker';


function Home() {


  return (
    <>
      <section className="MapContainer">
        <DatePicker/>
        <TooltipHeatMap />
        <InputForm/>
      </section>
    </>
  );
}

export default Home;
