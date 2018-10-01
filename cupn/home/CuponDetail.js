import React, { Component } from 'react';
import { TouchableOpacity,Alert, View, StyleSheet, Dimensions, Text, Image } from 'react-native';
import { TabView, TabBar, SceneMap } from 'react-native-tab-view';
import CuponList from "./CuponList";
import starbucks from '../images/starbucks.png'
import starbucksCup from '../images/starbucks_cup.png'
import qr from '../images/qrcode.png'




class CuponDetail extends Component {

    static navigationOptions = {
        title: 'Tu cupon',
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

    handleClick = (i) => {

    };

    openMap = () => {

    };

    constructor(props) {
        super(props);
        this.state = {


        };
    }

    render() {
        return (
            <View style={styles.container}>
                <View style={{ backgroundColor: '#fff', flex: 1, borderRadius:10, width: '100%'} }>
                    <View style={{height: 30, flexDirection: 'row', marginTop: 20, marginLeft: 20, alignItems: 'center'}}>
                        <Image style={{height: 50, width: 50,  }}
                               source={starbucks}/>
                        <Text style={{fontSize: 20, marginStart: 20}}>Starbucks</Text>
                        <Text style={{fontSize: 20, position: 'absolute', right: 20}}>A 0.2 KM</Text>
                    </View>
                    <View style={{height: 120,  alignItems: 'center', }}>
                        <Image style={{height: 200, width: 200,  marginTop: 100}}
                               source={qr}
                        />
                        <Text style={{fontSize: 15, color: '#000', marginTop: 60}}>Valido hasta el 10/10/2018</Text>
                    </View>

                    <TouchableOpacity activeOpacity={0.7} style={{position:'absolute', bottom: 20, height: 45, backgroundColor: '#067655', alignContent: 'center', alignItems: 'center',justifyContent: 'center',
                        borderRadius: 10, width: '90%', alignSelf: 'center',
                    }}
                                      onPress={this.openMap}>
                        <Text style={{fontSize: 20, textAlign: 'center', alignSelf: 'center', color: '#fff', fontWeight: 'bold'}}>Ver en mapa</Text>
                    </TouchableOpacity>
                </View>
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


export default CuponDetail;
