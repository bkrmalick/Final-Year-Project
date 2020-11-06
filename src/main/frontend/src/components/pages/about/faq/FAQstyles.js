import styled from 'styled-components'
import { animated } from 'react-spring'


const Frame = styled('div')`
  position: relative;
  padding: 4px 0px 0px 0px;
  text-overflow: ellipsis;
  min-width: 400px;
  overflow-x: hidden;
  vertical-align: middle;
  color: black;
  fill: black;

  @media (min-width: 960px){
    font-size: 20px;
  }
`

const Title = styled('span')`
  vertical-align: middle;
`

const Text = styled('p')`
  height:100%;
  color:white;
  border-radius: 5;
  padding:3%;
  
`

const Content = styled(animated.div)`
  will-change: transform, opacity, height;
  margin-left: 6px;
  padding: 0px 0px 0px 14px;
  border-left: 1px dashed rgba(255, 255, 255, 0.4);
  overflow: hidden;
`

const AnswerBox = styled('div')`
      position: relative; 
      width: 90%; 
      /*height: 100px;*/
      padding: 10px;
`

const TextContainingDiv = styled('div')`
      width: 100%; 
      height: 100%; 
      background: black;
      border-radius: 5px;
     
`

const toggle = {
  width: '1em',
  height: '1em',
  marginRight: 10,
  cursor: 'pointer',
  verticalAlign: 'middle'
}

export { AnswerBox, Frame, Content, toggle, Title, Text,TextContainingDiv }
