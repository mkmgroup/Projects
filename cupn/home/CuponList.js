import React from 'react';
import {Image, TouchableOpacity, Alert,StyleSheet, Text, View, FlatList, } from 'react-native';
import CuponCell from "./CuponCell";
import Modal from "react-native-modal";
import starbucks from '../images/starbucks.png'
import starbucksCup from '../images/starbucks_cup.png'



export default class CuponList extends React.Component {

    static propTypes = {

    };

    constructor(props) {
        super(props);
        this.state = {
            isModalVisible: false,
            item: {},
        };
    }

    componentDidMount(){

    }
    toggleModal = () =>
        this.setState({ isModalVisible: !this.state.isModalVisible });

    getCupon = () => {
        this.toggleModal();
        this.props.onPlay(this.state.item);
    };

    handlePlay = (i) => {
        this.toggleModal();
        this.setState({item: i});
    };

    render() {


        return (
            <View style={{flex: 1, width: '100%'}}>
                <FlatList style={styles.container}
                          data={dataGenders}
                          renderItem={({item}) => <CuponCell item={item} onPlay={(i) => this.handlePlay(i)}/>}
                />
                <Modal onBackButtonPress={this.toggleModal} onBackdropPress={this.toggleModal} isVisible={this.state.isModalVisible} style={{height: 300}}>
                    <View style={{ backgroundColor: '#fff', height: 300, borderRadius:10, } }>
                        <View style={{height: 30, flexDirection: 'row', marginTop: 20, marginLeft: 20, alignItems: 'center'}}>
                            <Image style={{height: 50, width: 50,  }}
                                   source={starbucks}/>
                            <Text style={{fontSize: 20, marginStart: 20}}>Starbucks</Text>
                            <Text style={{fontSize: 20, position: 'absolute', right: 20}}>A 0.2 KM</Text>
                        </View>
                        <View style={{height: 120, flexDirection: 'row', alignItems: 'center', justifyContent: 'center'}}>
                            <Image style={{height: 110, width: 70, marginRight: '25%', marginTop: 60}}
                                   source={starbucksCup}
                            />
                            <Text style={{fontSize: 64, color: '#000', marginTop: 60}}>10%</Text>
                        </View>

                        <TouchableOpacity activeOpacity={0.7} style={{position:'absolute', bottom: 20, height: 45, backgroundColor: '#067655', alignContent: 'center', alignItems: 'center',justifyContent: 'center',
                            borderRadius: 10, width: '90%', alignSelf: 'center',
                        }}
                                          onPress={this.getCupon}>
                            <Text style={{fontSize: 20, textAlign: 'center', alignSelf: 'center', color: '#fff', fontWeight: 'bold'}}>Canjear</Text>
                        </TouchableOpacity>
                    </View>
                </Modal>
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

const dataGenders = [{key: '1', color: '#000000',}, {key: '2', color: '#FAB42C', }, {key: '3', color: '#067655', },{key: '4', color: '#000000', }, {key: '5', color: '#000000',}, {key: '6', color: '#FAB42C', }, {key: '7', color: '#067655', },{key: '8', color: '#000000', }];