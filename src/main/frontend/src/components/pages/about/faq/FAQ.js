/*Credits: https://codesandbox.io/embed/lp80n9z7v9 */

import React, { memo, useState, useRef, useEffect } from 'react'
import { useSpring, a } from 'react-spring'
import { Text,TextContainingDiv, Frame, Title, Content, toggle, AnswerBox } from './FAQstyles'
import * as Icons from './FAQicons'

function usePrevious(value)
{
  const ref = useRef()
  useEffect(() => void (ref.current = value), [value])
  return ref.current
}

function useMeasure()
{
  const ref = useRef()
  const [bounds, set] = useState({ left: 0, top: 0, width: 0, height: 0 })
  const [ro] = useState(() => new ResizeObserver(([entry]) => set(entry.contentRect)))
  useEffect(() =>
  {
    if (ref.current) ro.observe(ref.current)
    return () => ro.disconnect()
    //eslint-disable-next-line
  }, [])
  return [{ ref }, bounds]
}

const Tree = memo(({ children, name, style, defaultOpen = false, link=undefined }) =>
{
  const [isOpen, setOpen] = useState(defaultOpen)
  const previous = usePrevious(isOpen)
  const [bind, { height: viewHeight }] = useMeasure()
  const { height, opacity, transform } = useSpring({
    from: { height: 0, opacity: 0, transform: 'translate3d(20px,0,0)' },
    to: { height: isOpen ? viewHeight : 0, opacity: isOpen ? 1 : 0, transform: `translate3d(${isOpen ? 0 : 20}px,0,0)` }
  })
  const Icon = Icons[`${children ? (isOpen ? 'Minus' : 'Plus') : 'Close'}SquareO`]
  return (
    <Frame>
      <Icon style={{ ...toggle, opacity: children ? 1 : 0.3 }} onClick={() => setOpen(!isOpen)} />
      {
        link ?
          <Title style={style}><a className= "tree-link"  href={link}>{name}</a></Title>
          :
        <Title style={style}>{name}</Title>
      }
      
      <Content style={{ opacity, height: isOpen && previous === isOpen ? 'auto' : height }}>
        <a.div style={{ transform }} {...bind} children={children} />
      </Content>
    </Frame>
  )
})

function FAQ(props) 
{
  return <>

    <Tree name="🦠 FAQs" defaultOpen>
      <Tree name="What's this app for? 🤔" defaultOpen={props.defaultOpen === "1" || props.defaultOpen === undefined}>
        <AnswerBox>
          <TextContainingDiv >
            <Text>To help you understand, visualise, and predict using official COVID data from <a className= "answer-box-link" target="_blank" rel="noopener noreferrer" href="https://data.london.gov.uk/dataset/coronavirus--covid-19--cases">the UK government itself!</a> </Text>
          </TextContainingDiv>
        </AnswerBox>
      </Tree>
      <Tree name="Who can use this?">
      <AnswerBox>
          <TextContainingDiv>
            <Text>Anyone! From people that are wanting to see and predict the cases counts in London to people curious about visualisation of API's</Text>
          </TextContainingDiv>
        </AnswerBox>
      </Tree>

      <Tree name="Why did you make this?">
      <AnswerBox>
          <TextContainingDiv >
            <Text>It's a part of my Final Year Project at Birkbeck, University of London <span role="img" aria-label="owl">🦉</span></Text>
          </TextContainingDiv>
        </AnswerBox>
      </Tree>

      <Tree name="Can I use your API for my project? 🤓" defaultOpen={props.defaultOpen === "4" }>
      <AnswerBox>
          <TextContainingDiv >
            <Text>Please get in touch with details of your project.</Text>
          </TextContainingDiv>
        </AnswerBox>
      </Tree>
      <Tree name="Where can I reach you?">
        <Tree name="📧 bkrmalick@hotmail.com" link={"mailto:bkrmalick@hotmail.com?subject=Regarding COVIDTRACKER Web App"}/>
        {/*<Tree name="sub-subtree with children">
          <Tree name="child 1" style={{ color: '#37ceff' }} />
          <Tree name="child 2" style={{ color: '#37ceff' }} />
          <Tree name="child 3" style={{ color: '#37ceff' }} />
          <Tree name="custom content">
            <TextContainingDiv>
              <div style={{ width: '100%', height: '100%', background: 'black', borderRadius: 5 }} />
            </TextContainingDiv>
          </Tree>
        </Tree>*/}
      </Tree>
    </Tree>
  </>
}

export default FAQ
