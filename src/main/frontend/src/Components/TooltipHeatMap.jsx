import React from 'react';
import Map from "../map2";
import { SVGMap } from "react-svg-map";

import './TooltipHeatMap.scss';
import TooltipText from './TooltipText';

//utils
import {getLocationName} from '../utils/MapUtils'
import {getCasesData} from '../utils/APIUtils'

class TooltipHeatMap extends React.Component {
	constructor(props) {
		super(props);

		this.state = {
			pointedLocation: null,
			tooltipStyle: {
				display: 'none'
			},
			casesData:null,
			casesDataRefreshDate:null,
			casesDataLoaded: false
		};

		this.handleLocationMouseOver = this.handleLocationMouseOver.bind(this);
		this.handleLocationMouseOut = this.handleLocationMouseOut.bind(this);
		this.handleLocationMouseMove = this.handleLocationMouseMove.bind(this);
	}

	componentDidMount()
	{
		//fetch data from API once map and its subcomponents have rendered 
		getCasesData().then(res=>
			{
				console.log(res);
				this.setState ( {
					casesDataRefreshDate:res.data.lastRefreshDate,
					casesDataLoaded: true,
					casesData:res.data.rows
				});
		
			})
			.catch(err=>console.log(err));
	
	}

	handleLocationMouseOver(event) {
		const pointedLocation = this.getTooltipText(event);
		this.setState({ pointedLocation });
	}

	getTooltipText(event) {
		
		var {casesData,casesDataLoaded} = this.state;
		const LOCATION_NAME=getLocationName(event);
		let CASES_DATA_RECORD={danger_percentage:null,cases_in_past_2_wks:null};

		if(casesDataLoaded)
			CASES_DATA_RECORD=casesData.filter(row=>row.area_name.toUpperCase()===LOCATION_NAME.toUpperCase())[0];

		return <TooltipText location={LOCATION_NAME} dangerLevel={CASES_DATA_RECORD.danger_percentage} casesInPastTwoWks={CASES_DATA_RECORD.cases_in_past_2_wks}/>;
	}

	handleLocationMouseOut() {
		this.setState({ pointedLocation: null, tooltipStyle: { display: 'none' } });
	}

	handleLocationMouseMove(event) {
		const tooltipStyle = {
			display: 'block',
			top: event.clientY + 10,
			left: event.clientX - 100
		};
		this.setState({ tooltipStyle });
	}

	getLocationClassName(location, index) {
		// Generate random heat map
		return `svg-map__location svg-map__location--heat${index % 4}`;
	}

	render() {

		var {casesDataRefreshDate,casesDataLoaded} = this.state;

		return (
			<article className="examples__block">
				<h2 className="examples__block__title">
					London Boroughs
				</h2>
				<div className="examples__block__map examples__block__map--london">
					<SVGMap
						map={Map}
						locationClassName={this.getLocationClassName}
						onLocationMouseOver={this.handleLocationMouseOver}
						onLocationMouseOut={this.handleLocationMouseOut}
						onLocationMouseMove={this.handleLocationMouseMove} />
					<div className="examples__block__map__tooltip" style={this.state.tooltipStyle}>
						{this.state.pointedLocation}
					</div>
				</div>
				
				<p className="examples__block__refreshDate">Valid as of { casesDataLoaded?casesDataRefreshDate:"Loading..."} </p>
			</article>
		);
	}
}

export default TooltipHeatMap;