// firebase.js

import { initializeApp } from 'firebase/app';
import { getMessaging, getToken, onMessage } from 'firebase/messaging';

const firebaseConfig = {
  apiKey: 'AIzaSyDzG0hZNZ_wERw95V7_EgbyLciEXjPD2Wc',
  projectId: 'plog-5ac65',
  messagingSenderId: '921732690597',
  appId: '1:921732690597:web:3fda0b21340a730fcfd34c',
};


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
        return currentToken;
      } else {
        return null;
      }
    } else {
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
    if (payload) {
      const { title, body } = payload.notification || payload.data;

      // 알림 생성
      new Notification(title, { body });
    } else {
      console.log("No message received.");
    }
  });
};
