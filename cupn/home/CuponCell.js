import React from 'react';
import { StyleSheet, Text, View,Image,TouchableOpacity, Alert } from 'react-native';
import PropTypes from 'prop-types';
import locationImage from '../images/location.png'
import starbucks from '../images/starbucks.png'

export default class CuponCell extends React.Component {

    static propTypes = {
        item: PropTypes.object,
    };

    constructor(props) {
        super(props);
        this.state = {

        };
    }

    componentDidMount(){

    }

    render() {
        return (
            <TouchableOpacity activeOpacity={0.7} onPress={() => this.props.onPlay(this.props.item)}>
                <View style={{backgroundColor: this.props.item.color,
                    alignSelf: 'center',
                    flex: 1,
                    height: 150,
                    marginTop: 10,
                    marginBottom: 10,
                    borderRadius: 10,
                    paddingRight: 10,
                    width: '90%'}}>


                    <View style={{height: 30, flexDirection: 'row', marginTop: 10, marginLeft: 20, alignItems: 'center' }}>
                        <Image style={{height: 15, width: 18,  }}
                               source={locationImage}/>
                        <Text style={{fontSize: 15, marginStart: 15, color: '#fff'}}>A 1,2 KM</Text>
                    </View>
                    <View style={{height: 80, flexDirection: 'row', alignItems: 'center', marginLeft: 20, }}>
                        <View style={{ marginTop: 10}}>
                            <Text style={{fontSize: 64, color: '#fff', marginTop: 0, fontWeight: 'bold'}}>10%</Text>
                            <Text style={{fontSize: 15, color: '#fff', marginTop: 0, fontWeight: 'bold', alignItems: 'center'}}>En todos nuestros productos</Text>
                        </View>

                        <Image style={{height: 70, width: 70,  marginTop: 0, right: 20, position: 'absolute'}}
                               source={starbucks}
                        />

                    </View>
                </View>
            </TouchableOpacity>

        );
    }
}

const styles = StyleSheet.create({

    container2: {
        alignItems: 'center',
        flex: 1,
        width: '100%',
        height: 400,
        alignContent: 'center',
        flexDirection: 'row'
    },
    title: {
        justifyContent : 'center',
        alignItems: 'center',
        alignContent : 'center',
        fontSize: 40,
        color: '#fff',
        fontWeight: 'bold',
    },
    listening: {
        fontSize: 18,
        color: '#fff',
        fontWeight: '100',
    }
});
