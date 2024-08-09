// firebase.js

import { initializeApp } from 'firebase/app';
import { getMessaging, getToken, onMessage } from 'firebase/messaging';

const firebaseConfig = {
  apiKey: 'AIzaSyDzG0hZNZ_wERw95V7_EgbyLciEXjPD2Wc',
  projectId: 'plog-5ac65',
  messagingSenderId: '921732690597',
  appId: '1:921732690597:web:3fda0b21340a730fcfd34c',
};

console.log('Firebase Config:', firebaseConfig);

// Initialize Firebase
const app = initializeApp(firebaseConfig);

// Initialize Firebase Cloud Messaging and get a reference to the service
const messaging = getMessaging(app);

export const requestForToken = async () => {
  try {
    const permission = await Notification.requestPermission();
    if (permission === 'granted') {
      const currentToken = await getToken(messaging, { vapidKey: process.env.REACT_APP_WEB_PUSH_CERTIFICATE_KEY });
      if (currentToken) {
        console.log('current token for client: ', currentToken);
        return currentToken;
      } else {
        console.log('No registration token available. Request permission to generate one.');
        return null;
      }
    } else {
      console.log('Notification permission was not granted.');
      return null;
    }
  } catch (err) {
    console.log('An error occurred while retrieving token. ', err);
    return null;
  }
};

// FCM foreground message listener
export const onForegroundMessage = () => {
  onMessage(messaging, (payload) => {
    console.log("Received foreground message: ", payload); // 메시지 수신 여부 확인
    if (payload) {
      const { title, body } = payload.notification || payload.data;
      new Notification(title, { body });
    } else {
      console.log("No message received.");
    }
  });
};
