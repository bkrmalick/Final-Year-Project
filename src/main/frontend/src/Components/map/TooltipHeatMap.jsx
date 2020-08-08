import React from 'react';
import LondonMap from "../../raw_map_files";
import './TooltipHeatMap.scss';

//other components
import  SVGMap  from "./svg-map";
import TooltipText from '../TooltipText';
import DatePicker from '../Datepicker';
import InputForm from '../InputForm';

//utils
import {getLocationName} from '../../utils/MapUtils'
import {getCasesData} from '../../utils/APIUtils'

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
		this.getLocationClassName = this.getLocationClassName.bind(this); 
	}

	componentDidMount()
	{
		//once map and its subcomponents have rendered 

		//1. fetch data from API and update state 
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

	handleLocationMouseOut() {
		this.setState({ pointedLocation: null, tooltipStyle: { display: 'none' } });
	}
	
	getDataRecordForArea(LOCATION_NAME, casesData)
	{
		return casesData.filter(row=>row.area_name.toUpperCase()===LOCATION_NAME.toUpperCase())[0];
	}

	//accepts either name of location to hover over OR the actual hover event
	getTooltipText(nameOrEvent) {
		
		var {casesData,casesDataLoaded} = this.state;
		const LOCATION_NAME= (typeof nameOrEvent)==="string" ? nameOrEvent: getLocationName(nameOrEvent);
		let CASES_DATA_RECORD={danger_percentage:"Loading",cases_in_past_2_wks:"Loading"};

		if(casesDataLoaded)
			CASES_DATA_RECORD=this.getDataRecordForArea(LOCATION_NAME, casesData);

		return <TooltipText location={LOCATION_NAME} dangerLevel={CASES_DATA_RECORD.danger_percentage} casesInPastTwoWks={CASES_DATA_RECORD.cases_in_past_2_wks}/>;
	}

	handleLocationMouseMove(event) {
		const tooltipStyle = {
			display: 'block',
			top: event.clientY + 10,
			left: event.clientX - 100
		};
		this.setState({ tooltipStyle });
	}
	
	getLocationClassName(location, index) 
	{
		let CASES_DATA_RECORD;
		if(!this.state.casesDataLoaded)
		{
			CASES_DATA_RECORD={danger_percentage:null,cases_in_past_2_wks:null};
		}
		else
		{
			CASES_DATA_RECORD=this.getDataRecordForArea(location.id, this.state.casesData);
		}

		let heatNumber= ((CASES_DATA_RECORD.danger_percentage/100)*4).toFixed(0);
		heatNumber=(heatNumber==="4")?"3":heatNumber;
		

		// Generate heat map acc. to danger percentage of area
		return `svg-map__location svg-map__location--heat${heatNumber}`;
	}

	triggerHover(LOCATION_NAME)
	{
		let ev = new Event('Hover', { bubbles: true});
		ev.simulated = true;
		this.dispatch(ev);
		
	}

	render() {

		var {casesDataRefreshDate,casesDataLoaded} = this.state;

		return (
			<>
			<article className="MapContainer__block">
			<DatePicker date={casesDataRefreshDate}/><br/>
				<h2 className="MapContainer__block__title">
					London Boroughs
				</h2>
				<div className="MapContainer__block__map MapContainer__block__map--london">
					<SVGMap
						map={LondonMap}
						locationClassName={this.getLocationClassName}
						onLocationMouseOver={this.handleLocationMouseOver}
						onLocationMouseOut={this.handleLocationMouseOut}
						onLocationMouseMove={this.handleLocationMouseMove}
						onLocationClick={e=>console.log(e.target.id)} />
					<div className="MapContainer__block__map__tooltip" style={this.state.tooltipStyle}>
						{this.state.pointedLocation}
					</div>
				</div>
				
				<p className="MapContainer__block__refreshDate">Using dataload of { casesDataLoaded?casesDataRefreshDate:"Loading..."} </p>
			</article>
			<InputForm triggerHover={this.triggerHover}/>

			</>
		);
	}
}

export default TooltipHeatMap;