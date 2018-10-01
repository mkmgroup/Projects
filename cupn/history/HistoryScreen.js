import React from 'react';
import { StyleSheet, Text, View, Image} from 'react-native';
import {createStackNavigator, createBottomTabNavigator,} from "react-navigation";
import CuponDetail from "../home/CuponDetail";

import History from "./History";


const HistoryScreen = createStackNavigator(
    {
        History: { screen: History },
        HistoryDetail: { screen: CuponDetail },
    },
    {
        initialRouteName: 'History',
    }

);




export default HistoryScreen;