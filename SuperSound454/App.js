import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import GenderList from "./GenderList";

export default class App extends React.Component {

    static navigationOptions = {
        title: 'SuperSound',
        headerStyle: {
            backgroundColor: '#ffffff',
            elevation: 0,
        },
        headerTintColor: '#000',
        headerTitleStyle: {
            fontWeight: 'bold',
            flex: 1,
            textAlign: 'center',
            alignSelf: 'center',
        },
    };

    handlePlay = (i) => {

        this.props.navigation.navigate('Details', {
            item: i,
        });
    };

    render() {
        return (
            <View style={styles.container}>
                <GenderList onPlay={(i) => this.handlePlay(i)}/>
            </View>
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
