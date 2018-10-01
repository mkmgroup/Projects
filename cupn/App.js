import React from 'react';
import { StyleSheet, Text, View, Image} from 'react-native';
import {createStackNavigator, createBottomTabNavigator,} from "react-navigation";
import Home from './home/Home'
import CuponDetail from "./home/CuponDetail";
import HistoryScreen from "./history/HistoryScreen";
import MapScreen from "./map/MapScreen";
import list from './images/list.png'
import unselectedList from './images/unselected/list.png'
import map from './images/placeholder.png'
import unselectedMap from './images/unselected/placeholder.png'
import voucher from './images/voucher.png'
import unselectedVoucher from './images/unselected/voucher.png'


const appStyles = StyleSheet.create({
    iconImage: {
        width: 15,
        height: 15,
    }
});

const App = createStackNavigator(
    {
        Home: { screen: Home },
        Detail: { screen: CuponDetail },
    },
    {
        initialRouteName: 'Home',
    }

);


export default createBottomTabNavigator(
    {
        Home: App,
        Map: MapScreen,
        History: HistoryScreen,
    },

    {   navigationOptions: ({ navigation }) => ({
            tabBarIcon: ({ focused, tintColor }) => {
                const { routeName } = navigation.state;
                let iconName;
                if (routeName === 'Home') {
                    return <Image source={focused ? voucher : unselectedVoucher}  style={{width: 30, height: 18}} />
                } else if (routeName === 'History') {
                    return <Image source={focused ? list : unselectedList}  style={{width: 18, height: 23}} />
                }else if (routeName === 'Map') {
                    return <Image source={focused ? map : unselectedMap}  style={{width: 25, height: 25}} />
                }

                // You can return any component that you like here! We usually use an
                // icon component from react-native-vector-icons

            },
        }),
        tabBarOptions: {
            activeTintColor: 'black',
            inactiveTintColor: 'gray',
            labelStyle:{height: 0,}
        },
    }
);


