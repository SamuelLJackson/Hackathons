/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import TimerMixin from 'react-timer-mixin';
import {
  Platform,
  StyleSheet,
  Text,
      Animated,
      TouchableHighlight,
      View,
      Image,
      TouchableOpacity,
      Modal,
      NavigatorIOS,
      Button,
      Dimensions,
} from 'react-native';

import PropTypes from 'prop-types';
import Swiper from 'react-native-swiper';
import {StackNavigator} from 'react-navigation';
import MapView from 'react-native-maps';
import isEqual from 'lodash/isEqual';

import Menu, {
    MenuContext,
	MenuOptions,
	MenuOption,
  MenuTrigger
	} from 'react-native-popup-menu';


const GEOLOCATION_OPTIONS = { enableHighAccuracy: true, timeout: 20000, maximumAge: 1000 };
const ANCHOR = { x: 0.5, y: 0.5 };
const colorOfmyLocationMapMarker = 'blue';

const propTypes = {
    ...MapView.Marker.propTypes,
    // override this prop to make it optional
    coordinate: PropTypes.shape({
	    latitude: PropTypes.number.isRequired,
	    longitude: PropTypes.number.isRequired,
	}),
    children: PropTypes.node,
    geolocationOptions: PropTypes.shape({
	    enableHighAccuracy: PropTypes.bool,
	    timeout: PropTypes.number,
	    maximumAge: PropTypes.number,
	}),
    heading: PropTypes.number,
    enableHack: PropTypes.bool,
};

const defaultProps = {
    enableHack: false,
    geolocationOptions: GEOLOCATION_OPTIONS,
};

const {width,height} = Dimensions.get('window');
const ASPECT_RATIO = width / height;
const LATITUDE = 35.955407;
const LONGITUDE = -83.935405;
const LATITUDE_DELTA = 0.0422;
const LONGITUDE_DELTA = LATITUDE_DELTA * ASPECT_RATIO;
let id = 0;
global.checkedIn = false;
global.checkedInLocation = null;
global.showCheckIn = true;
global.showCheckOut = false;

const HomeScreen = ({ navigation}) => 
    (
     <View style={styles.home_screen}>


     <TouchableOpacity onPress={()=>navigation.navigate('Map',{type:'commuters',jacobType:'c'})}>
     <Text style={styles.button1}>Commuter</Text>
     </TouchableOpacity>
     
     <TouchableOpacity onPress={()=>navigation.navigate('Map',{type:'nonCommuters',jacobType:'n'})}>
     <Text style={styles.button1}>Non-Commuter</Text>
     </TouchableOpacity>
     <TouchableOpacity onPress={()=>navigation.navigate('Map',{type:'motorcycles',jacobType:'m'})}>
     <Text style={styles.button1}>Motorcycle</Text>
     </TouchableOpacity>
     
     <TouchableOpacity onPress={()=>navigation.navigate('Map',{type:'visitors'})}>
     <Text style={styles.button1}>Visitor</Text>
     </TouchableOpacity>				       
     <TouchableOpacity onPress={()=>navigation.navigate('Map',{type:'facultyStaff'})}>
     <Text style={styles.button1}>Faculty/Staff</Text>
     </TouchableOpacity>
     </View>
);


class ParkingLotInfoScreen extends React.Component {
    constructor(props) {

	super(props);
	this.mounted = true;
	this.state = {
	    isLoading: true,
	    title: '',
	    spots_available: '',
	};
    }
    componentDidMount() {
	/*
	return fetch('http://54.227.216.77/polygons', {
		method: 'POST',
		    headers: {
		    'Accept': 'application/json',
			'Content-Type': 'application/json',
			},
		    body: JSON.stringify({
			    polygon_id: this.props.navigation.state.params.id,
				})
		    })
	    .then((response)=>response.json())
	    .then((responseJson) => {
		    this.setState({
			    isLoading: false,
			    title: responseJson.title,
			    spots_available: responseJson.spots_available,
			    
			}, function() {
			});
		})
	    .catch((error) => {
		    console.error(error);
		});
	*/
	return fetch('http://18.220.71.215:3000/parking/'+this.props.navigation.state.params.id)
	    .then((response)=>response.json())
	    .then((responseJson) => {
		    this.setState({
			    isLoading: false,
			    title: responseJson[0].title,
			    spots_available: responseJson[0].spots_available,
			}, function() {
			    
			    if (responseJson[0].title == checkedInLocation){
				showCheckIn = true;
			    }
			});
		})
	    .catch((error)=>{
		    console.error(error);
		});
    }

    componentWillUnmount() {
	this.mounted = false;

    }
    incrementSpotsAvailable(amount) {
	return fetch('http://18.220.71.215:3000/parking/'+this.props.navigation.state.params.id,{
	    method: 'PUT',
		    headers: {
		    'Accept':'application/json',
		    'Content-Type': 'application/json',
			},
		    body: JSON.stringify({spots_available: amount})
		    })
	    .then((response)=>response.json())
	    .then((responseJson) => {
		    this.setState({
			    isLoading: false,
			    title: responseJson.name,
			    spots_available: responseJson.spots_available,
			}, function() {
			    var fColor = null;
			    var sColor = null;
			    if (responseJson.spots_available == 10) {
				fColor = 'rgba(255,0,0,0.5)';
				sColor = '#FF0000';
			    } else if (responseJson.spots_available == 11){
				fColor = 'rgba(0,255,0,0.5)';
				sColor = '#00FF00';
			    }
			    return fetch('http://18.220.71.215:3000/parking/color/'+this.props.navigation.state.params.id, {
				    method: 'PUT',
				    headers: {
					'Accept':'application/json',
					'Content-Type': 'application/json',
					
				    },
				    body: JSON.stringify({
					    fillColor: fColor,
					    strokeColor: sColor,
					})
				})
			    .then((response)=>response.json())
			    .then((responseJson) => {
				    this.setState({
					    isLoading: false,
					}, function() {
					});
				})
			    .catch((error) => {
				    console.error(error);
				});
			});
		})
	    .catch((error) => {
		    console.error(error);
		});
    }
    /*
    incrementSpotsAvailable(amount) {
	return fetch('http://54.227.216.77/polygonsUpdate', {
		method: 'POST',
		    headers: {
		    'Accept': 'application/json',
			'Content-Type': 'application/json',
			},
		    body: JSON.stringify({
			    polygon_id: this.props.navigation.state.params.id,
			    amount: amount,
				})
		    })
	    .then((response)=>response.json())
	    .then((responseJson) => {
		    this.setState({
			    isLoading: false,
			    title: responseJson.name,
			    spots_available: responseJson.spots_available,
			    
			}, function() {
			});
		})
	    .catch((error) => {
		    console.error(error);
		});

    }
    */

    render() {
	const {state} = this.props.navigation;
	return(
		<View style={{
			flex: 1,
			flexDirection:'column',
			justifyContent:'center',
			alignItems: 'center',
			backgroundColor: '#FFA500',
		    }}>


<View>
		<Text style={styles.text}>
		    {state.params.title}{'\n'}

		Spots Available:  {this.state.spots_available}
		</Text>

		<TouchableOpacity onPress={()=> this.incrementSpotsAvailable(-1)}>
		<Text style={styles.button1}>Check In</Text>
		</TouchableOpacity>

		<TouchableOpacity onPress={()=> this.incrementSpotsAvailable(1)}>
		<Text style={styles.button1}>Check Out</Text>
		</TouchableOpacity>
</View>
         </View>
		   );
    }
    

}


class DefaultMarkers extends React.Component {

    constructor(props) {
	super(props);
	//this.mounted = false;
	this.state = {
	    region: {
		latitude: LATITUDE,
		longitude: LONGITUDE,
		latitudeDelta: LATITUDE_DELTA,
		longitudeDelta: LONGITUDE_DELTA,
	    },
	    markers: [
		      ],
	    polygons: [],
	    modalVisible:false,
	    myPosition: null,
	};
    }
    mxins: [TimerMixin];
    componentDidMount() {
	this.mounted = true;

	this.watchLocation();
	/*
	this.interval = setInterval(()=> {
		this.refresher();
	    },5000);//5 seconds	
	*/
	//return fetch('http://54.227.216.77/'+this.props.navigation.state.params.type)
	var fetchString = 'http://18.220.71.215:3000/parking/lots/'+this.props.navigation.state.params.jacobType;
	if (this.props.navigation.state.params.type == 'facultyStaff') {
	    fetchString = 'http://54.227.216.77/static/facultyStaff.json';
	} else if (this.props.navigation.state.params.type == 'visitors') {
	    fetchString = 'http://54.227.216.77/static/visitors.json';
	} else if (this.props.navigation.state.params.type == 'motorcycles') {
	    fetchString = 'http://54.227.216.77/static/motorcycles.json';
	}
	return fetch(fetchString)
	    .then((response)=>response.json())
	    .then((responseJson) => {
		    this.setState({
			    isLoading: false,
			    polygons: responseJson.polygons,
			}, function() {
			    for (j=0; j < this.state.polygons.length; j++) {
				if (this.state.polygons[j].spots_available < 11) {
				    this.state.polygons[j].strokeColor = "#FF0000";
				    this.state.polygons[j].fillColor = "rgba(255,0,0,0.5)";
				} else {
				    this.state.polygons[j].strokeColor = "#00FF00";
				    this.state.polygons[j].fillColor = "rgba(0,255,0,0.5)";
				}
			    }
	


			});
		})
	    .catch((error) => {
		    console.error(error);
		});

    }
    

    watchLocation() {
	this.watchID = navigator.geolocation.watchPosition((position) => {
		const myLastPosition = this.state.myPosition;
		const myPosition = position.coords;
		if (!isEqual(myPosition, myLastPosition)) {
		    this.setState({myPosition});
		}
	    }, null, this.props.geolocationOptions);
    }
    
    componentWillUnmount() {
	this.mounted = false;
	
	navigator.geolocation.clearWatch(this.watchID);
    }


    _pressPolygon(currentPolygon) {
	const {navigate} = this.props.navigation;
	navigate('ParkingLotInfo',
		 {title:currentPolygon.name,
			 id:currentPolygon.id});
    }
    onMapPress(e) {
	this.setState({
		markers: [
			  ...this.state.markers,
			  {
			      coordinate: e.nativeEvent.coordinate,
				  key: id++,
				  color: randomColor(),
				  },
			  ],
		    });
    }
    setModalVisible(visible,currentMarker) {
	
	this.setState({
		modalVisible: visible,
		    modalTitle: currentMarker.title,
		    modalAvailability: currentMarker.description,
		    });
    }
    setModalInvisible() {
	this.setState({
		modalVisible: false,
		    });
    }
    refresher() {
	this.mounted = false;
	//return fetch('http://54.227.216.77/'+this.props.navigation.state.params.type)
	return fetch('http://18.220.71.215:3000/parking/lots/'+this.props.navigation.state.params.jacobType)
	    .then((response)=>response.json())
	    .then((responseJson) => {
		    this.setState({
			    isLoading: false,
			    polygons: responseJson.polygons,
			}, function() {
			    for (j=0; j < this.state.polygons.length; j++) {
				if (this.state.polygons[j].spots_available < 11) {
				    this.state.polygons[j].strokeColor = "#FF0000";
				    this.state.polygons[j].fillColor = "rgba(255,0,0,0.5)";

				} else {
				    this.state.polygons[j].strokeColor = "#00FF00";
				    this.state.polygons[j].fillColor = "rgba(0,255,0.5)";
				}

			    }
			});
		})
	    .catch((error) => {
		    console.error(error);
		});
	this.mounted = true;

    }
    render() {
	let {heading, coordinate} = this.props;
	if (!coordinate) {
	    const {myPosition} = this.state;
	    if (!myPosition) return null;
	    coordinate = myPosition;
	    heading = myPosition.heading;
	}
	

	const rotate = (typeof heading === 'number' && heading >= 0) ? `${heading}deg` : null;
	return (
	<View style={styles.container}>
        <MapView
		provider={this.props.provider}
		style={styles.map}
		initialRegion={this.state.region}>

		{this.state.polygons.map(polygon => (

						     <MapView.Polygon
						     key={polygon.id}
						     coordinates={polygon.coordinates}
						     strokeColor={polygon.strokeColor}
						     fillColor={polygon.fillColor}
						     strokeWidth={1}

						     onPress={()=> this._pressPolygon(polygon)}


						     />

						     ))}


	      <MapView.Marker
        anchor={ANCHOR}
        style={styles.mapMarker}
		  {...this.props}
        coordinate={coordinate}
      >
        <View style={styles.myLocationContainer}>
	<View style={styles.markerHalo} />
	    {rotate &&
		    <View style={[styles.heading, { transform: [{ rotate }] }]}>
		    <View style={styles.headingPointer} />
            </View>
		  }
	<View style={styles.marker}>
	<Text style={{ width: 0, height: 0 }}>
	    {this.props.enableHack && rotate}
            </Text>
          </View>
        </View>
	    {this.props.children}
      </MapView.Marker>

        </MapView>
        <View style={styles.buttonContainer}>
          <TouchableOpacity
	onPress={() => this.refresher()}
	style={styles.bubble}
          >
            <Text>Refresh</Text>
          </TouchableOpacity>
        </View>
</View>

		);
    }
}
DefaultMarkers.propTypes = {
    provider: MapView.ProviderPropType,
};


class MapScreen extends React.Component {
    constructor(props) {
	super(props);
	this.state = {
	    isLoading: true,
	    region: null,
	    myPosition: null,
	}
    }
    getInitialState() {
	return {
	    coordinate: new MapView.AnimatedRegion({
		    latitude: LATITUDE,
			longitude: LONGITUDE,
	    }),
	    region: {
		latitude: 37.78825,
		    longitude: -122.4324,
		    latitudeDelta: 0.0922,
		    longitudeDelta: 0.0421,
		    },
		};
    }
    componentWillReceiveProps(nextProps) {
	if (this.props.coordinate !== nextProps.coordinate) {
	    this.state.coordinate.timing({
		    ...nextProps.coordinate,
			duration: 500
		}).start();
	}
    }
    onRegionChange(region) {
	this.setState({ region });
    }

    render() {
	return (

  <MapView
  initialRegion={{
			latitude: 37.78825,
			longitude: -122.4324,
			latitudeDelta: 0.0922,
			longitudeDelta: 0.0421,
		    }}>
  <MapView.Marker.Animated coordinate={this.state.coordinate}/>
</MapView>

		);
    }
}


const RootNavigator = StackNavigator({
	Home: {
	    screen: HomeScreen,
	    navigationOptions:{
		title: 'Home',
		headerStyle: {backgroundColor: '#000'},
		headerTitleStyle: {color: '#fff'}
	    },
	},

	Map: {
	    screen: DefaultMarkers,
	    navigationOptions:{
		headerTitle: 'Map',
		headerStyle: {backgroundColor: '#000'},
		headerTitleStyle: {color: '#fff'}
	    },
	},
	ParkingLotInfo: {
	    screen: ParkingLotInfoScreen,
	    navigationOptions: {
		headerTitle: 'Lot Info',
		headerStyle: {backgroundColor: '#000'},
		headerTitleStyle: {color: '#fff'}
	    },
	},
    });

export default RootNavigator;


const SIZE = 20;
const HALO_RADIUS = 6;
const ARROW_SIZE = 7;
const ARROW_DISTANCE = 6;
const HALO_SIZE = SIZE + HALO_RADIUS;
const HEADING_BOX_SIZE = HALO_SIZE + ARROW_SIZE + ARROW_DISTANCE;


const styles = StyleSheet.create({
	
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },

  text: {
	    color: '#fff',
	    fontSize: 30,
	    fontWeight: 'bold',
  },
  slide1: {
	    flex: 1,
	    justifyContent: 'center',
	    alignItems: 'center',
	    backgroundColor: '#FFA500',
  },
  button1: {
	    color: '#fff',
	    fontWeight: 'bold',
	    fontSize: 30,
	    paddingLeft: 30,
	    paddingTop: 30,
	    backgroundColor: '#FFA500',
	    borderColor: '#fff',
	    borderWidth: 15,
	    margin:10,
  },
  checkInButton: {
	    color: '#fff',
	    fontWeight: 'bold',
	    fontSize: 30,
	    paddingLeft: 90,
	    paddingTop: 20,
	    backgroundColor: '#FFA500',
	    borderColor: '#fff',
	    borderWidth: 15,
	    margin:10,
  },
  counterButton: {
	    color: '#fff',
	    fontWeight: 'bold',
	    fontSize: 50,
	    paddingLeft: 110,
	    paddingTop: 15,
	    backgroundColor: '#FFA500',
	    borderColor: '#fff',
	    borderWidth: 15,
	    margin:10,
  },
  home_screen: {
	    flex: 1, 
	    backgroundColor: '#FFA500',
	    alignItems: 'center', 
	    justifyContent: 'center' 
  },
  container_OLD: {
      ...StyleSheet.absoluteFillObject,
      justifyContent: 'flex-end',
      alignItems: 'center',
  },
  container2: {
	    position: 'absolute',
	    top: 0,
	    left: 0,
	    right: 0,
	    bottom: 0,
	    justifyContent: 'flex-end',
	    alignItems: 'center',
  },
  map: {
	    position: 'absolute',
	    top: 0,
	    left: 0,
	    right: 0,
	    bottom: 0,
  },
  map_OLD: {
      ...StyleSheet.absoluteFillObject,
  },
  bubble: {
	    backgroundColor: 'rgba(255,255,255,0.7)',
	    paddingHorizontal: 18,
	    paddingVertical: 12,
	    borderRadius: 20,
  },
  latlng: {
	    width: 200,
	    alignItems: 'stretch',
  },
  button: {
	    width: 80,
	    paddingHorizontal: 12,
	    alignItems: 'center',
	    marginHorizontal: 10,
  },
  
  buttonContainer: {
	    flex:1,
	    flexDirection: 'column',
	    marginVertical: 20,
	    backgroundColor: 'transparent',
  },

  mapMarker: {
	    zIndex: 1000,
  },
  // The container is necessary to protect the markerHalo shadow from clipping
  myLocationContainer: {
	    width: HEADING_BOX_SIZE,
	    height: HEADING_BOX_SIZE,
  },
  heading: {
	    position: 'absolute',
	    top: 0,
	    left: 0,
	    width: HEADING_BOX_SIZE,
	    height: HEADING_BOX_SIZE,
	    alignItems: 'center',
  },
  headingPointer: {
	    width: 0,
	    height: 0,
	    backgroundColor: 'transparent',
	    borderStyle: 'solid',
	    borderTopWidth: 0,
	    borderRightWidth: ARROW_SIZE * 0.75,
	    borderBottomWidth: ARROW_SIZE,
	    borderLeftWidth: ARROW_SIZE * 0.75,
	    borderTopColor: 'transparent',
	    borderRightColor: 'transparent',
	    borderBottomColor: colorOfmyLocationMapMarker,
	    borderLeftColor: 'transparent',
  },
  markerHalo: {
	    position: 'absolute',
	    backgroundColor: 'white',
	    top: 0,
	    left: 0,
	    width: HALO_SIZE,
	    height: HALO_SIZE,
	    borderRadius: Math.ceil(HALO_SIZE / 2),
	    margin: (HEADING_BOX_SIZE - HALO_SIZE) / 2,
	    shadowColor: 'black',
	    shadowOpacity: 0.25,
	    shadowRadius: 2,
	    shadowOffset: {
		height: 0,
		width: 0,
	    },
  },
  marker: {
	    justifyContent: 'center',
	    backgroundColor: colorOfmyLocationMapMarker,
	    width: SIZE,
	    height: SIZE,
	    borderRadius: Math.ceil(SIZE / 2),
	    margin: (HEADING_BOX_SIZE - SIZE) / 2,
  },
    });