import React from 'react'
import LondonMap from "../../../../map files"
import './TooltipHeatMap.scss'

//other components
import  SVGMap  from "./svg-map"
import TooltipText from '../TooltipText'
import DatePicker from '../Datepicker'
import PostCodeForm from '../PostCodeForm'
import Popup from 'react-popup'
import ErrorIcon from '../../../ErrorIcon'

//utils
import {getLocationName} from '../../../../utils/MapUtils'
import { getCasesDataForDate } from '../../../../utils/APIUtils'

import ClipLoader from "react-spinners/ClipLoader"

class TooltipHeatMap extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			pointedLocation: null,
			tooltipStyle: {
				display: 'none'
			},
			casesData:null,
			casesDataRefreshDate: null, 
			casesDataDate: null,
			casesDataMode:null,
			casesDataLoaded: false, //false: not loaded, true: loaded, null: error while fetching data
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
		this.setSelectedLocationCoordinates = this.setSelectedLocationCoordinates.bind(this);
		this.setCasesDataDate = this.setCasesDataDate.bind(this);
		this.unselectSelectedLocation = this.unselectSelectedLocation.bind(this);
	}

	componentDidMount()
	{
		//once map and its subcomponents have rendered 

		//fetch data from API and update state for date selected by user (if any)
		if(!this.state.casesDataLoaded) 
		{
			getCasesDataForDate(this.state.casesDataDate).then(res=>
				{
					console.log(res);
					this.setState ( {
						casesDataRefreshDate: res.data.data_last_refreshed,
						casesDataDate: res.data.date,
						casesDataLoaded: true,
						casesData: res.data.cases_data,
						casesDataMode:res.data.mode
					});
			
				})
				.catch(err => {
					console.log(err.response)
				
					Popup.alert("Sorry, there was an error while fetching the data from the server. Please contact admin.");

					this.setState({
							casesDataLoaded:null //error
					});
				}); 
		}
	}

	componentDidUpdate(prevProps, prevState)
	{
		if(!this.state.casesDataLoaded) 
		{
			getCasesDataForDate(this.state.casesDataDate).then(res=>
				{
					console.log(res);
					this.setState ( {
						casesDataRefreshDate: res.data.data_last_refreshed,
						casesDataDate: res.data.date,
						casesDataLoaded: true,
						casesData: res.data.cases_data,
						casesDataMode:res.data.mode
					});
				
					if (this.state.casesDataMode === "Prediction" && this.state.casesDataMode !== prevState.casesDataMode)
					{
						Popup.alert("The application will now extrapolate existing data up until the selected date. ", "Switched to Prediction Mode");
					}
			
				})
				.catch(err =>
				{
					console.log(err);

					const errorDuringIntialLoad = (this.state.casesDataLoaded === null);
					if (!errorDuringIntialLoad)
					{
						Popup.alert(err.response.data.message + ". Reverting to original selection.");

						//reset to previous state
						this.setState({
							casesDataDate: prevState.casesDataDate,
							casesDataLoaded: prevState.casesDataLoadedd
						});
					}
				});
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

	handleLocationMouseOver(nameOrEvent)
	{
		const pointedLocation = this.getTooltipText(nameOrEvent);
		this.setState({ pointedLocation });
	}

	handleLocationMouseOut()
	{
		this.setState({ pointedLocation: null, tooltipStyle: { display: 'none' } });
	}
	
	handleLocationMouseMove(event) 
	{
		this.unselectSelectedLocation(); 

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
		let CASES_DATA_RECORD={relative_danger_percentage:"Loading",cases_in_past_2_wks:"Loading"};

		if(casesDataLoaded)
			CASES_DATA_RECORD=this.getDataRecordForArea(LOCATION_NAME, casesData);

		return <TooltipText location={LOCATION_NAME} dangerLevel={CASES_DATA_RECORD.relative_danger_percentage} casesInPastTwoWks={CASES_DATA_RECORD.cases_in_past_2_wks}/>;
	}
	
	getLocationClassName(location, index) 
	{	
		// add code here to make each location class dynamic

		return `svg-map__location svg-map__location`;
	}

	//sets color of heatmap acc. to the danger percentage
	getLocationStyles(location)
	{
		let CASES_DATA_RECORD;
		let IS_SELECTED;
		const hasCasesDataLoaded = (this.state.casesDataLoaded);
		const hasCasesDataLoadErrored = (this.state.casesDataLoadedd===null);
		
		if (this.state.selectedLocationName === null)
		{
			IS_SELECTED = true;
		}
		else
		{
			IS_SELECTED = this.state.selectedLocationName.toUpperCase() === location.id.toUpperCase();	
		}
		
		if(!hasCasesDataLoaded)
		{
			CASES_DATA_RECORD = { relative_danger_percentage: null, cases_in_past_2_wks: null };
		}
		else
		{
			CASES_DATA_RECORD = this.getDataRecordForArea(location.id, this.state.casesData);
		}

		//only display when data has been loaded
		return {
			fill: this.heatMapColorforValue(IS_SELECTED, CASES_DATA_RECORD.relative_danger_percentage),
			display: !hasCasesDataLoaded || hasCasesDataLoadErrored  ? "none" : "block"
		};
	}

 	getLoadingWheelStyles()
	{
		let {casesDataLoaded} = this.state;
		//only display when data is loading
		return {
			margin: "20%" ,
			display: casesDataLoaded || casesDataLoaded ===null ? "none" : "inline-block"
		};
	}

	getErrorIconStyles()
	{
		let {casesDataLoaded} = this.state;
		
		return {
			Zposition: 4, //high z-index so errorIcon appears infront of svg
			position: 'relative',
			display: casesDataLoaded === null ? "inline-block" : "none", //only display when data load has errored
			margin: "23%"
		};
	}

	/*
		Returns the RGB color for the location given its danger percentage 
		and a boolean stating whether its selected by the user
	*/
	heatMapColorforValue(isSelected,value)
	{
		let RED, GREEN, BLUE;
		
		if (!isSelected)
		{
			//B&W
			RED = 100 - value; GREEN = 100 - value; BLUE = 100 - value; 
		}
		else
		{
			//RED
			RED = 100; GREEN = 100 - value; BLUE = 100-value; 
		}
	
		return "rgb("+RED+"%,"+GREEN+"%, "+BLUE+"%)";
	}

	setSelectedLocationName(LOCATION_NAME)
	{
		this.setState({selectedLocationName:LOCATION_NAME}); 
	}

	/*
		Sets the selectedLocationCoordinates 
		and returns boolean describing 
		whether location coordinates have been set and if the svg needs to be refit
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
				top: (coor.top + (coor.bottom-coor.top)/2 ) ,
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

	unselectSelectedLocation()
	{
		if (this.state.selectedLocationName !== null)
		{
			const tooltipStyle = {
				display: 'none'
			};

			this.setState({ selectedLocationName: null, selectedLocationCoordinates: null,tooltipStyle }); //unselect any location
		}
	}

	setCasesDataDate(date)
	{
		this.setState({
			casesDataLoaded: false,
			casesDataDate: date
			});
	}

	render() {
		let { casesDataDate, casesDataMode, casesDataRefreshDate, casesDataLoaded } = this.state;
	
		return (
			<>
      <section className="MapContainer">
			<article className="MapContainer__block" >
					
					<h2 className="MapContainer__block__title">
							London Boroughs
					</h2>
						
						<DatePicker className="MapContainer__block__dateBox"
							date={casesDataDate}
							setDate={this.setCasesDataDate}
							mode={casesDataMode}
							casesDataRefreshDate={this.state.casesDataRefreshDate}
						/>
						<p className="MapContainer__block__headline">Hover over a Borough to view its COVID case counts</p>
						
						<div className="MapContainer__block__map MapContainer__block__map--london" style={{ width: "50vw", height: "25vw" }} > 
								<SVGMap
									map={LondonMap}
									locationClassName={this.getLocationClassName}
									locationStyles={this.getLocationStyles}
									onLocationMouseOver={this.handleLocationMouseOver}
									onLocationMouseOut={this.handleLocationMouseOut}
									onLocationMouseMove={this.handleLocationMouseMove}
									isLocationSelected={this.isLocationSelected}
									setSelectedLocationCoordinates={this.setSelectedLocationCoordinates}
									dataLoaded={this.casesDataLoaded}
									//onLocationClick={e=>console.log(e.target.id)} 
									className="svg-map" />	
									<ClipLoader css={this.getLoadingWheelStyles()} />
									<ErrorIcon css={this.getErrorIconStyles()} />
						
							<div className="MapContainer__block__map__tooltip" style={this.state.tooltipStyle}>
								{this.state.pointedLocation}
							</div>
				</div>
					
				<p className="MapContainer__block__refreshDate">Using dataload of { casesDataLoaded?casesDataRefreshDate: casesDataLoaded===null?"⚠":"Loading..."} </p>
			</article>
					<PostCodeForm setSelectedLocationName={this.setSelectedLocationName} unselectSelectedLocation={this.unselectSelectedLocation}/>
		</section>
			</>
		);
	}
}

export default TooltipHeatMap;