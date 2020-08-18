import React from 'react';

import VideoHeroSection from './section-types/VideoHeroSection';
import InfoHeroSection from './section-types/HeroSection';
import { homeObjOne, homeObjTwo } from './data/HomeData';

import Footer from '../footer/Footer';

function Home() {
  return (
    <div>
      <VideoHeroSection/>
      <InfoHeroSection {...homeObjOne}/>
      <InfoHeroSection {...homeObjTwo} />
      <Footer/>
    </div>
  );
}

export default Home;
