//source: https://github.com/chrvadala/react-svg-pan-zoom/tree/master/src

import React from "react";

let uid = 1;
const nextUID = () => `uid${uid++}`;

export default function RandomUID(WrappedComponent) {
  class RandomUID extends React.Component {
    constructor(props) {
      super(props)
      this.state = {uid: nextUID()}
    }

    render() {
      return <WrappedComponent _uid={this.state.uid} {...this.props}/>
    }
  }

  RandomUID.displayName = `RandomUID(${getDisplayName(WrappedComponent)})`;

  return RandomUID;
}


function getDisplayName(WrappedComponent) {
  return WrappedComponent.displayName || WrappedComponent.name || 'Component';
}