import React from 'react';
import PropTypes from 'prop-types';
import { ReactSVGPanZoom, TOOL_NONE, zoom } from 'react-svg-pan-zoom';
import { useState } from 'react';

function SVGMap(props) {

	const [tool, setTool] = useState(TOOL_NONE);
	//below intial value sets position/size of svg inside viewer
	const [value, setValue] = useState({SVGHeight: 385,SVGMinX: 0,SVGMinY: 0,SVGWidth: 800,a: 1.2727272727272727,b: 0,c: 0,d: 1.2727272727272727,e: 50.27272727272731,endX: null,endY: null,f: 21.363636363636374,focus: false,lastAction: null,miniatureOpen: true,mode: "idle",pinchPointDistance: null,prePinchMode: null,scaleFactorMax: null,scaleFactorMin: null,startX: null,startY: null,version: 3, 		viewerHeight: 539, 		viewerWidth: 1120});

	return (
		<ReactSVGPanZoom
			width={800} height={385} //sets the dimensions of the viewer and not the svg map (will increase itself to fit intial value)  //TODO make this responsive to screen size
			onClick={event => { console.log(event.x, event.y, event.originalEvent); zoom(472, 128, 1.5); }} //TODO not working?? ? 
			tool={tool}
			onChangeTool={t => setTool(t)}
			value={value}
			onChangeValue={v => {console.log(v);setValue(v); }}
			detectAutoPan={false}
			preventPanOutside={true}
			background="white" //TODO change to white
			className="MapContainer__block__map MapContainer__block__map__ViewerBox"
			toolbarProps={{
				activeToolColor: "LightCoral"
			}}

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
							name={location.name}
							d={location.path}
							className={typeof props.locationClassName === 'function' ? props.locationClassName(location, index) : props.locationClassName}
							tabIndex={typeof props.locationTabIndex === 'function' ? props.locationTabIndex(location, index) : props.locationTabIndex}
							role={props.locationRole}
							aria-label={location.name}
							aria-checked={props.isLocationSelected && props.isLocationSelected(location, index)}
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