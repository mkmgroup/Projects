import React, { Component } from 'react';
import {Alert, View, StyleSheet, Dimensions } from 'react-native';
import { TabView, TabBar, SceneMap } from 'react-native-tab-view';
import CuponList from "./CuponList";




class Home extends Component {
    FirstRoute = () => (
        <View style={[styles.container, { backgroundColor: '#ffffff' }]}>
            <CuponList onPlay={(i) => this.handleClick(i)} />
        </View>
    );
    SecondRoute = () => (
        <View style={[styles.container, { backgroundColor: '#673ab7' }]} />
    );
    ThirdRoute = () => (
        <View style={[styles.container, { backgroundColor: '#33b74a' }]} />
    );
    FouthRoute = () => (
        <View style={[styles.container, { backgroundColor: '#3422b7' }]} />
    );
    FifthRoute = () => (
        <View style={[styles.container, { backgroundColor: '#b705af' }]} />
    );
    static navigationOptions = {
        title: 'Cupn',
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
        this.props.navigation.navigate('Detail')
    };

    constructor(props) {
        super(props);
        this.state = {
            index: 0,
            routes: [
                { key: 'first', title: 'Cercanos' },
                { key: 'second', title: 'Gastronomia' },
                { key: 'third', title: 'Indumentaria' },
                { key: 'fourth', title: 'Electronica' },
                { key: 'fifth', title: 'Servicios' },
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
                        scrollEnabled={true}
                        tabStyle={{width: 150}}
                    />
                }
                navigationState={this.state}
                renderScene={SceneMap({
                    first: this.FirstRoute,
                    second: this.SecondRoute,
                    third: this.ThirdRoute,
                    fourth: this.FouthRoute,
                    fifth: this.FifthRoute,
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


export default Home;
