import React from 'react';
import PropTypes from 'prop-types';
import { ReactSVGPanZoom, TOOL_NONE  } from 'react-svg-pan-zoom';
import { useState } from 'react';
import {AutoSizer} from 'react-virtualized';
import { useRef } from 'react';

function SVGMap(props) {

	const [tool, setTool] = useState(TOOL_NONE);
	
	const [value, setValue] = useState({}); //describes position/size of svg inside viewer
	const viewerRef  = useRef({}); //reference to the viewer
	const [isFirstRender, setIsFirstRender] = useState(true);

	if(viewerRef.current.fitToViewer!==undefined && isFirstRender) //for first few renders the viewerRef does not have this function
	{	
		viewerRef.current.fitToViewer("center", "center");
		setIsFirstRender(false);
	}

	function setSelectedLocationElement(pathRef)
	{
		if(pathRef!==null)
			props.setSelectedLocationCoordinates(pathRef);
	}

	/*function useTraceUpdate(props) {
		const prev = useRef(props);
		useEffect(() => {
		  const changedProps = Object.entries(props).reduce((ps, [k, v]) => {
			if (prev.current[k] !== v) {
			  ps[k] = [prev.current[k], v];
			}
			return ps;
		  }, {});
		  if (Object.keys(changedProps).length > 0) {
			console.log('Changed props:', changedProps);
		  }
		  prev.current = props;
		});
	  }
	  
	  useTraceUpdate(props);*/

return (
		<AutoSizer>
		  {(({width, height}) => width === 0 || height === 0 ? null : (

		

			<ReactSVGPanZoom
			width={width-5} height={height-5} //sets the dimensions of the viewer and not the svg map (will increase itself to fit intial value)  //TODO make this padded/margin instead of 5
				//onClick={event => { console.log(event.x, event.y, event.originalEvent); zoom(472, 128, 1.5); }} //TODO zoom not working?? ? 
				tool={tool}
				onChangeTool={t => setTool(t)}
				value={value}
				onChangeValue={v => setValue(v) }
				detectAutoPan={false}
				preventPanOutside={true}
				background="white" //TODO change to white
				className="MapContainer__block__map MapContainer__block__map__ViewerBox"
				toolbarProps={{	activeToolColor: "LightCoral"}}
				ref={viewerRef}
			>
				<svg
					xmlns="http://www.w3.org/2000/svg"
					viewBox={"0 0 800 385"} //controls the dimensions of the actual svg map, ensure this matches the one defined in the map/index.js
					className={props.className}
					role={props.role}
					aria-label={props.map.label}
				>
					{props.childrenBefore}
					{props.map.locations.map((location, index) => {
						return (
							<path
						id={location.id}
	//							name={location.name}
						d={location.path}
						className={typeof props.locationClassName === 'function' ? props.locationClassName(location, index) : props.locationClassName}
						tabIndex={typeof props.locationTabIndex === 'function' ? props.locationTabIndex(location, index) : props.locationTabIndex}
	//							role={props.locationRole}
	//							aria-label={location.id}
						aria-checked={props.isLocationSelected && props.isLocationSelected(location.id)}
						ref={ (props.isLocationSelected && props.isLocationSelected(location.id)) ? setSelectedLocationElement : null}
						onMouseOver={props.onLocationMouseOver}
						onMouseOut={props.onLocationMouseOut}
						onMouseMove={props.onLocationMouseMove}
						onClick={props.onLocationClick}
						onKeyDown={props.onLocationKeyDown}
						onFocus={props.onLocationFocus}
						onBlur={props.onLocationBlur}
						key={location.id}
							/>
						);
					})}
					{props.childrenAfter}
				</svg>
			</ReactSVGPanZoom>
		  ))}
		</AutoSizer>
		);
}

SVGMap.propTypes = {
	// Map properties
	map: PropTypes.shape({
		viewBox: PropTypes.string.isRequired,
		locations: PropTypes.arrayOf(
			PropTypes.shape({
				path: PropTypes.string.isRequired,
				id: PropTypes.string.isRequired,
				name: PropTypes.string
			})
		).isRequired,
		label: PropTypes.string
	}).isRequired,
	className: PropTypes.string,
	role: PropTypes.string,

	// Locations properties
	locationClassName: PropTypes.oneOfType([PropTypes.string, PropTypes.func]),
	locationTabIndex: PropTypes.oneOfType([PropTypes.string, PropTypes.func]),
	locationRole: PropTypes.string,
	onLocationMouseOver: PropTypes.func,
	onLocationMouseOut: PropTypes.func,
	onLocationMouseMove: PropTypes.func,
	onLocationClick: PropTypes.func,
	onLocationKeyDown: PropTypes.func,
	onLocationFocus: PropTypes.func,
	onLocationBlur: PropTypes.func,
	isLocationSelected: PropTypes.func,

	// Slots
	childrenBefore: PropTypes.node,
	childrenAfter: PropTypes.node,
};

SVGMap.defaultProps = {
	className: 'svg-map',
	role: 'none', // No role for map
	locationClassName: 'svg-map__location',
};

export default SVGMap;