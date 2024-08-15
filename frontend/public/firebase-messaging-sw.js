importScripts('https://www.gstatic.com/firebasejs/8.10.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.10.0/firebase-messaging.js');

const firebaseConfig = {
  apiKey: 'AIzaSyDzG0hZNZ_wERw95V7_EgbyLciEXjPD2Wc',
  projectId: 'plog-5ac65',
  messagingSenderId: '921732690597',
  appId: '1:921732690597:web:3fda0b21340a730fcfd34c',
};

firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();

messaging.onBackgroundMessage(function(payload) {

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

  event.waitUntil(
    clients.matchAll({ type: 'window', includeUncontrolled: true }).then(windowClients => { // type: 'all'로 변경
      let matchedClient = null;

      // for (let i = 0; i < windowClients.length; i++) {
      //   const client = windowClients[i];

      //   // 'https://i11b308.ip.ssafy.io/'를 포함하는 탭이나 앱을 찾습니다.
      //   if (client.url.startswith('주소')) {
      //       matchedClient = client;  // 클라이언트 객체를 저장합니다.
      //       break;
      //   }

      // }

      if (matchedClient) {
        // 해당 탭이나 앱이 있으면 포커스하고 해당 URL로 리다이렉트
        return matchedClient.focus().then(() => {
          return matchedClient.navigate(click_action);
        });
      } else {
        // 해당 탭이나 앱이 없으면 새로운 창이나 앱을 엽니다.
        return self.clients.openWindow(click_action);
      }
    })
  );
});

