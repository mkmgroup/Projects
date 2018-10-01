import {
    createStackNavigator,
} from 'react-navigation';
import App from "./App";
import PlayGender from "./PlayGender";

const SuperApp = createStackNavigator(
    {
        Home: { screen: App },
        Details: { screen: PlayGender },
    },
    {
        initialRouteName: 'Home',
    }

);

export default SuperApp;