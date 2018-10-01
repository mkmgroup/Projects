import React from 'react';
import { StyleSheet, Text, View,Image,TouchableOpacity, Alert } from 'react-native';
import PropTypes from 'prop-types';
import playIcon from './icons/play.png'
import stopIcon from './icons/stop.png'

export default class GenderItem extends React.Component {

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
            <View style={{backgroundColor: this.props.item.color,
                alignItems: 'center',
                alignSelf: 'center',
                flex: 1,
                height: 400,
                alignContent: 'center',
                marginTop: 10,
                marginBottom: 10,
                borderRadius: 10,
                width: '90%'}}>
                <View style={styles.container2}>
                    <Text style={styles.title}>{this.props.item.key}</Text>
                    <Text style={styles.listening}>1 2 3 escuchando</Text>
                    <Image
                        style={{height: 300, width: '100%',}}
                        source={{uri: 'http://www.stickpng.com/assets/thumbs/5852df35394e280271f3b48e.png'}}
                    />
                </View>
                <TouchableOpacity
                    activeOpacity={0.7}
                    onPress={() => this.props.onPlay(this.props.item)}
                    style={{
                        borderWidth:0,
                        borderColor:'rgba(0,0,0,0.2)',
                        alignItems:'center',
                        justifyContent:'center',
                        width:60,
                        height:60,
                        backgroundColor:'#fff',
                        borderRadius:100,
                        position: 'absolute',
                        top: 300,

                    }}
                >
                    <Image
                        style={{height: 30, width: 30,}}
                        source={playIcon}
                    />

                </TouchableOpacity>
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
        flex: 1,
        width: '100%',
        height: 400,
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
    }
});
