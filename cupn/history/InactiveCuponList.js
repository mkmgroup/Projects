import React from 'react';
import {Image, TouchableOpacity, Alert,StyleSheet, Text, View, FlatList, } from 'react-native';
import CuponCell from "../home/CuponCell";



export default class InactiveCuponList extends React.Component {

    static propTypes = {

    };

    constructor(props) {
        super(props);
        this.state = {

        };
    }

    componentDidMount(){

    }



    handlePlay = (i) => {
        this.props.onPlay(i);
    };

    render() {


        return (
            <View style={{flex: 1, width: '100%'}}>
                <FlatList style={styles.container}
                          data={dataGenders}
                          renderItem={({item}) => <CuponCell item={item} onPlay={(i) => this.handlePlay(i)}/>}
                />

            </View>



        );
    }
}

const styles = StyleSheet.create({
    container: {
        marginTop: 0,
        width: '100%',
    },
});

const dataGenders = [{key: '1', color: '#707070',}, {key: '2', color: '#707070', }, {key: '3', color: '#707070', },{key: '4', color: '#707070', }, {key: '5', color: '#707070',}, {key: '6', color: '#707070', }, {key: '7', color: '#707070', },{key: '8', color: '#707070', }];