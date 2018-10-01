import React, { Component } from 'react';
import {Alert, View, StyleSheet, Dimensions } from 'react-native';
import { TabView, TabBar, SceneMap } from 'react-native-tab-view';

import ActiveCuponList from "./ActiveCuponList";
import InactiveCuponList from "./InactiveCuponList";




class History extends Component {
    FirstRoute = () => (
        <View style={[styles.container, { backgroundColor: '#ffffff' }]}>
            <ActiveCuponList onPlay={(i) => this.handleClick(i)} />
        </View>
    );
    SecondRoute = () => (
        <View style={[styles.container, { backgroundColor: '#ffffff' }]} >
            <InactiveCuponList onPlay={(i) => this.handleInactiveClick(i)} />
        </View>
    );

    static navigationOptions = {
        title: 'Historial',
        headerStyle: {
            backgroundColor: '#ffffff',
            elevation: 0,
            height: 0,
        },
        headerTintColor: '#000',
        headerTitleStyle: {
            fontWeight: 'bold',
            flex: 1,
            textAlign: 'center',
            alignSelf: 'center',
        },
    };

    handleClick = (i) => {
        this.props.navigation.navigate('HistoryDetail')
    };

    handleInactiveClick = (i) => {
    };


    constructor(props) {
        super(props);
        this.state = {
            index: 0,
            routes: [
                { key: 'first', title: 'Activos' },
                { key: 'second', title: 'Vencidos' },
            ],
        };
    }

    render() {
        return (
            <TabView
                renderTabBar={props =>
                    <TabBar
                        {...props}
                        indicatorStyle={{ backgroundColor: '#FAB42C' }}
                        style={{backgroundColor: 'white'}}
                        labelStyle={{color: '#000', fontWeight: 'bold'}}
                    />
                }
                navigationState={this.state}
                renderScene={SceneMap({
                    first: this.FirstRoute,
                    second: this.SecondRoute,
                })}
                onIndexChange={index => this.setState({ index })}
                initialLayout={{ width: Dimensions.get('window').width, height: 0 }}
            />
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    },
});


export default History;
