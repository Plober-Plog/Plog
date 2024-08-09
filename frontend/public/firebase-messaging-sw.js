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

messaging.onBackgroundMessage(function(payload) {
  console.log('[firebase-messaging-sw.js] Received background message ', payload);

  let notificationTitle = payload.data.title || 'Default Title';
  let notificationOptions = {
    body: payload.data.message || 'Default Body',
    icon: payload.data.icon || '/firebase-logo.png',
    data: {
      click_action: payload.data.click_action || '/',
    }
  };

  self.registration.showNotification(notificationTitle, notificationOptions);
});

self.addEventListener('notificationclick', function(event) {
  const click_action = event.notification.data.click_action;
  event.notification.close(); // 알림을 닫음

  console.log('Notification Clicked:', click_action);

  event.waitUntil(
    clients.matchAll({ type: 'window', includeUncontrolled: true }).then(windowClients => {
      let matchedClient = null;

      for (let i = 0; i < windowClients.length; i++) {
        const client = windowClients[i];
        console.log('Checking client URL:', client.url);

        // 'https://i11b308.ip.ssafy.io/'를 포함하는 탭을 찾습니다.
        if (client.url.includes('/')) {
            console.log('**** include client URL:', client.url);
            matchedClient = click_action;
            break;
        }

      }

      if (matchedClient) {
        // 해당 탭이 있으면 포커스하고 해당 URL로 리다이렉트
        console.log('Focusing and navigating to:', click_action);
        return matchedClient.focus();
      } else {
        // 해당 탭이 없으면 새로운 탭을 엽니다.
        console.log('No matching client found. Opening new window:', click_action);
        return self.clients.openWindow("/");
      }
    })
  );
});
