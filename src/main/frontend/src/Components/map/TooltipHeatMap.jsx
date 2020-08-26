import React from 'react';
import LondonMap from "../../map files";
import './TooltipHeatMap.scss';

//other components
import  SVGMap  from "./svg-map";
import TooltipText from '../TooltipText';
import DatePicker from '../Datepicker';
import PostCodeForm from '../PostCodeForm';

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
			casesDataLoaded: false,
			selectedLocationName:null,
			selectedLocationCoordinates:null
		};

		this.handleLocationMouseOver = this.handleLocationMouseOver.bind(this);
		this.handleLocationMouseOut = this.handleLocationMouseOut.bind(this);
		this.handleLocationMouseMove = this.handleLocationMouseMove.bind(this);
		this.getLocationClassName = this.getLocationClassName.bind(this); 
		this.getLocationStyles = this.getLocationStyles.bind(this); 
		this.setSelectedLocationName = this.setSelectedLocationName.bind(this); 
		this.isLocationSelected = this.isLocationSelected.bind(this); 
		this.setSelectedLocationCoordinates=this.setSelectedLocationCoordinates.bind(this);
	}

	componentDidMount()
	{
		//once map and its subcomponents have rendered 

		//1. fetch data from API and update state 
		if(!this.state.casesDataLoaded)
		{
			getCasesData().then(res=>
				{
					console.log(res);
					this.setState ( {
						casesDataRefreshDate:res.data.data_last_refreshed,
						casesDataLoaded: true,
						casesData:res.data.cases_data
					});
			
				})
				.catch(err=>console.log(err));
		}
	}

	/*
	componentDidUpdate(prevProps, prevState) {
		Object.entries(this.props).forEach(([key, val]) =>
		  prevProps[key] !== val && console.log(`Prop '${key}' changed`)
		);
		if (this.state) {
		  Object.entries(this.state).forEach(([key, val]) =>
			prevState[key] !== val && console.log(`State '${key}' changed`)
		  );
		}
	  }
	*/

	handleLocationMouseOver(nameOrEvent) {
		const pointedLocation = this.getTooltipText(nameOrEvent);
		this.setState({ pointedLocation });
	}

	handleLocationMouseOut() {
		this.setState({ pointedLocation: null, tooltipStyle: { display: 'none' } });
	}
	
	handleLocationMouseMove(event) 
	{
		this.setState({selectedLocationName:null, selectedLocationCoordinates:null}); //unselect any location

		const tooltipStyle = {
			display: 'block',
			top: event.clientY + 10,
			left: event.clientX - 100
		};
		this.setState({ tooltipStyle });
	}

	getDataRecordForArea(LOCATION_NAME, casesData)
	{
		return casesData.filter(row=>row.area_name.toUpperCase()===LOCATION_NAME.toUpperCase())[0];
	}

	isLocationSelected(LOCATION_NAME)
	{
		//if no location has been selected then mark all as selected (e.g first render)
		return this.state.selectedLocationName==null || this.state.selectedLocationName===LOCATION_NAME;
	}

	//accepts either name of location to hover over OR the actual hover event
	getTooltipText(nameOrEvent) 
	{
		var {casesData,casesDataLoaded} = this.state;
		const LOCATION_NAME= (typeof nameOrEvent)==="string" ? nameOrEvent: getLocationName(nameOrEvent);
		let CASES_DATA_RECORD={danger_percentage:"Loading",cases_in_past_2_wks:"Loading"};

		if(casesDataLoaded)
			CASES_DATA_RECORD=this.getDataRecordForArea(LOCATION_NAME, casesData);

		return <TooltipText location={LOCATION_NAME} dangerLevel={CASES_DATA_RECORD.danger_percentage} casesInPastTwoWks={CASES_DATA_RECORD.cases_in_past_2_wks}/>;
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

	//sets color of heatmap acc. to the danger percentage
	getLocationStyles(location)
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

		return { fill: this.heatMapColorforValue(CASES_DATA_RECORD.danger_percentage) };
	}

	heatMapColorforValue(value)
	{
		//const RED = 100-value, GREEN = 100-value, BLUE = 100-value; //B&W
		const RED = 100, GREEN = 100-value, BLUE = 100-value; //RED

		return "rgb("+RED+"%,"+GREEN+"%, "+BLUE+"%)";
	}

	setSelectedLocationName(LOCATION_NAME)
	{
		this.setState({selectedLocationName:LOCATION_NAME}); 
	}

	/*
		Sets the selectedLocationCoordinates and returns whether location coordinates have been set and if the svg needs to be refit
	*/
	setSelectedLocationCoordinates(el)
	{
		const {selectedLocationName, selectedLocationCoordinates,tooltipStyle} = this.state;
		const coor=el.getBoundingClientRect(); //see object spec here: https://developer.mozilla.org/en-US/docs/Web/API/Element/getBoundingClientRect
	
		if(this.state.selectedLocationName==null) //first render where no post code inputted
			return false;

		 if(selectedLocationCoordinates===null || (coor.left !== tooltipStyle.left) ) 
		{	
			let newTooltipStyle = {
				display: 'block',
				top: (coor.top + (coor.bottom-coor.y)/2 ) ,
				left: (coor.left ) //+ (coor.right-coor.x)/2
			};

			this.setState({ tooltipStyle :newTooltipStyle });

			this.handleLocationMouseOver(selectedLocationName); 

			this.setState({selectedLocationCoordinates:coor});

			return true;
		} 
		else
		{
			return false;
		}
	}

	render() {

		var {casesDataRefreshDate,casesDataLoaded} = this.state;

		return (
			<>
      <section className="MapContainer">
			<article className="MapContainer__block" >
					
					<h2 className="MapContainer__block__title">
							London Boroughs
					</h2>
					<DatePicker className="MapContainer__block__dateBox" date={casesDataRefreshDate} /><br/>
				
				<div className="MapContainer__block__map MapContainer__block__map--london" style={{ width: "50vw", height: "25vw" }}> {/*TODO make heigh/width proportional*/}
		
							<SVGMap
								map={LondonMap}
								locationClassName={this.getLocationClassName}
								locationStyles={this.getLocationStyles}
								onLocationMouseOver={this.handleLocationMouseOver}
								onLocationMouseOut={this.handleLocationMouseOut}
								onLocationMouseMove={this.handleLocationMouseMove}
								isLocationSelected={this.isLocationSelected}
								setSelectedLocationCoordinates={this.setSelectedLocationCoordinates}
								//onLocationClick={e=>console.log(e.target.id)} 
								className="svg-map"
							/>
						
							<div className="MapContainer__block__map__tooltip" style={this.state.tooltipStyle}>
								{this.state.pointedLocation}
							</div>
				</div>
					
				<p className="MapContainer__block__refreshDate">Using dataload of { casesDataLoaded?casesDataRefreshDate:"Loading..."} </p>
			</article>
				<PostCodeForm setSelectedLocationName={this.setSelectedLocationName} />
		</section>
			</>
		);
	}
}

export default TooltipHeatMap;