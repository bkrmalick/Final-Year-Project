import React from 'react';
import renderer from 'react-test-renderer';
import App from '../App'
import About from '../components/pages/about/About';
import HelpUsCollect from '../components/pages/help-us-collect/HelpUsCollect';
import Home from '../components/pages/home/Home';
import Tracker from '../components/pages/tracker/Tracker';
import { BrowserRouter as Router } from 'react-router-dom';

it("App renders correctly", () =>
{
  const componentRender = renderer
    .create(<App />)
    .toJSON();
  expect(componentRender).toMatchSnapshot();
});

it("Home page renders correctly", () =>
{
  const componentRender = renderer
    .create(<Router><Home /></Router>)
    .toJSON();
  expect(componentRender).toMatchSnapshot();
});

it("Tracker page renders correctly", () =>
{
  const componentRender = renderer
    .create(<Tracker />)
    .toJSON();
  expect(componentRender).toMatchSnapshot();
});

it("About page renders correctly", () =>
{

  const mockMatch = {
    params: {
      id: "0"   // /about/{questionNumberToExpand}
    }
  };

  const componentRender = renderer
    .create(<About match={mockMatch} />)
    .toJSON();
  expect(componentRender).toMatchSnapshot();
});

it("COVID survey page renders correctly", () =>
{
  const componentRender = renderer
    .create(<HelpUsCollect />)
    .toJSON();
  expect(componentRender).toMatchSnapshot();
});
