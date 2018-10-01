import React from 'react';
import { StyleSheet, Text, View,Image,TouchableOpacity, Alert, Linking } from 'react-native';
import Modal from "react-native-modal";
import PropTypes from 'prop-types';
import playIcon from './icons/play.png'
import stopIcon from './icons/stop.png'
import starbucks from './images/starbucks.png'
import starbucksCup from './images/starbucks_cup.png'
import Video from 'react-native-video';

export default class PlayGender extends React.Component {


    static navigationOptions = ({ navigation }) => {
        const item = navigation.getParam('item', 'NO-ID');
        return{
            title: '',
            headerStyle: {
                backgroundColor: item.color,
                elevation: 0,
            },
            headerTintColor: '#fff',
            headerTitleStyle: {
                fontWeight: 'bold',
                textAlign: 'center',
                alignSelf: 'center',
            },
        }
    };

    constructor(props) {
        super(props);
        this.state = {
            paused: false,
            icon: stopIcon,
            isModalVisible: false,
        };
    }

    handlePlay = () => {
          this.setState({
              paused: !this.state.paused,
              icon: this.state.paused ? stopIcon : playIcon,
          })
    };
    _toggleModal = () =>{
        this.setState({ isModalVisible: !this.state.isModalVisible });
        let url = 'http://www.cupn.com/id/123';
        Linking.openURL(url).catch(err => console.error('An error occurred', err));
    };


    componentDidMount(){
        setTimeout(() => {this.setState({isModalVisible: true})}, 5000)
    }

    render() {

        const { navigation } = this.props;
        const item = navigation.getParam('item', 'NO-ID');

        return (
            <View style={{backgroundColor: item.color,
                alignItems: 'center',
                alignSelf: 'center',
                flex: 1,
                height: '100%',
                alignContent: 'center',
                width: '100%'}}>
                <View style={styles.container2}>
                    <Text style={styles.title}>{item.key}</Text>
                    <Text style={styles.listening}>1 2 3 escuchando</Text>
                    <Image
                        style={{height: 300, width: '100%',}}
                        source={{uri: 'http://www.stickpng.com/assets/thumbs/5852df35394e280271f3b48e.png'}}
                    />
                </View>
                <TouchableOpacity
                    activeOpacity={0.7}
                    onPress={() => this.handlePlay()}
                    style={{
                        borderWidth:0,
                        borderColor:'rgba(0,0,0,0.2)',
                        alignItems:'center',
                        justifyContent:'center',
                        width:60,
                        height:60,
                        backgroundColor:'#fff',
                        borderRadius:100,
                        top: 20,
                    }}
                >
                    <Image
                        style={{height: 30, width: 30,}}
                        source={this.state.icon}
                    />

                </TouchableOpacity>
                <Video source={{uri: item.url}} // Can be a URL or a local file.
                       ref="audioElement"
                       paused={this.state.paused}               // Pauses playback entirely
                       style={styles.audioElement} />

                <Modal isVisible={this.state.isModalVisible} style={{height: 300}}>
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
                                          onPress={this._toggleModal}>
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
        backgroundColor: '#000',
        alignItems: 'center',
        alignSelf: 'center',
        flex: 1,
        height: 400,
        alignContent: 'center',
        marginTop: 10,
        marginBottom: 10,
        borderRadius: 10,
        width: '90%'    },
    container2: {
        alignItems: 'center',
        width: '100%',
        alignContent: 'center',
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
    },
    audioElement: {
        height: 0,
        width: 0,
    }
});
