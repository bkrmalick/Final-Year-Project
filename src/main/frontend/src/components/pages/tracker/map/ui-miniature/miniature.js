//source: https://github.com/chrvadala/react-svg-pan-zoom/tree/master/src

function set(value, patch, action = null) {
  value = Object.assign({}, value, patch, {lastAction: action});
  return Object.freeze(value);
}


export function openMiniature(value) {
  return set(value, {
    miniatureOpen: true
  });
}

export function closeMiniature(value) {
  return set(value, {
    miniatureOpen: false
  });
}

