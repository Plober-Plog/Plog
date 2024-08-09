importScripts('https://www.gstatic.com/firebasejs/8.10.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.10.0/firebase-messaging.js');

const firebaseConfig = {
  apiKey: 'AIzaSyDzG0hZNZ_wERw95V7_EgbyLciEXjPD2Wc',
  projectId: 'plog-5ac65',
  messagingSenderId: '921732690597',
  appId: '1:921732690597:web:3fda0b21340a730fcfd34c',
};

console.log('Firebase Config:', firebaseConfig);

firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();

// messaging.onBackgroundMessage(function(payload) {
//   console.log('[firebase-messaging-sw.js] Received background message ', payload);

//   let notificationTitle = 'Default Title';
//   let notificationOptions = {
//     body: 'Default Body',
//     icon: '/firebase-logo.png'
//   };

//   if (payload.notification) {
//     notificationTitle = payload.notification.title;
//     notificationOptions.body = payload.notification.body;
//   } else if (payload.data) {
//     notificationTitle = payload.data.title || notificationTitle;
//     notificationOptions.body = payload.data.body || notificationOptions.body;
//   }

//   self.registration.showNotification(notificationTitle, notificationOptions);
// });

messaging.onBackgroundMessage();
