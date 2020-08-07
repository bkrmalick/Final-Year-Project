import React from 'react';
import TooltipHeatMap from './TooltipHeatMap';
import InputForm from './InputForm';



function Home() {


  return (
    <>
      <section className="MapContainer">
        <TooltipHeatMap />
        <InputForm/>
      </section>
    </>
  );
}

export default Home;
