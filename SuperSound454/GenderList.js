import React from 'react';
import { Alert,StyleSheet, Text, View, FlatList, } from 'react-native';
import GenderItem from "./GenderItem";
import Video from 'react-native-video';




export default class GenderList extends React.Component {

    static propTypes = {

    };

    constructor(props) {
        super(props);
        this.state = {
            paused: true,
            playingItem: '',
            playingUrl: '',
        };
    }

    componentDidMount(){

    }

    handlePlay = (i) => {

        /*this.props.navigation.navigate('Details', {
            item: i,
        });*/

        this.props.onPlay(i);

        /*this.setState({
            paused: !this.state.paused,
            playingUrl: i.url,
        })*/
    };

    render() {


        return (
            <View style={{flex: 1, width: '100%'}}>
                <FlatList style={styles.container}
                          data={dataGenders}
                          renderItem={({item}) => <GenderItem item={item} onPlay={(i) => this.handlePlay(i)}/>}
                />
                <Video source={{uri: this.state.playingUrl}} // Can be a URL or a local file.
                       ref="audioElement"
                       paused={this.state.paused}               // Pauses playback entirely
                       style={styles.audioElement} />
            </View>



        );
    }
}

const styles = StyleSheet.create({
    container: {
        marginTop: 0,
        width: '100%',
    },
    audioElement: {
        height: 0,
        width: 0,
    }
});

const dataGenders = [{key: 'Rock', color: '#000000', url: 'http://163.172.198.16:8014/;stream.mp3'}, {key: 'Electronica', color: '#FAB42C', url: 'http://streaming01.shockmedia.com.ar:9716/;stream'}, {key: 'Pop', color: '#067655', url: 'http://198.178.123.14:7002/;'},{key: 'Latino', color: '#000000', url: 'http://str2.openstream.co/540?aw_0_1st.stationid=3077&aw_0_1st.publisherId=564&aw_0_1st.serverId=str2&listenerid=1531504310782_0.5271192262301723&awparams=companionAds%3Atrue&aw_0_1st.version=1.1.4%3Ahtml5'}];