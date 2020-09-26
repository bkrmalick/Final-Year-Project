import React from 'react';

import VideoHeroSection from './section-types/VideoHeroSection';
import InfoHeroSection from './section-types/HeroSection';
import { homeObjOne, homeObjTwo } from './data/HomeData';

function Home() {
  return (
    <div>
      <VideoHeroSection/>
      <InfoHeroSection {...homeObjOne}/>
      <InfoHeroSection {...homeObjTwo} />
    </div>
  );
}

export default Home;
