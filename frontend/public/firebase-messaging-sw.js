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

  let notificationTitle = payload.notification?.title || 'Default Title';
  let notificationOptions = {
    body: payload.notification?.body || 'Default Body',
    icon: payload.data?.icon || '/firebase-logo.png',  // payload에서 아이콘을 받아오거나 기본 아이콘 설정
    data: {
      click_action: payload.data?.click_action || '', // 클릭 시 이동할 URL 설정
    }
  };

  // self.registration.showNotification(notificationTitle, notificationOptions);
});

// messaging.onBackgroundMessage();

self.addEventListener('notificationclick', function(event) {
  const click_action = event.notification.data.click_action;
  event.notification.close(); // 알림을 닫음

  event.waitUntil(
    clients.matchAll({ type: 'window', includeUncontrolled: true }).then(windowClients => {
      // 열려있는 브라우저 창이 있는지 확인
      for (let i = 0; i < windowClients.length; i++) {
        const client = windowClients[i];
        // 현재 창이 이미 열려 있는지 확인
        if (client.url === click_action && 'focus' in client) {
          return client.focus();
        }
      }
      // 창이 열려 있지 않다면 새 창을 염
      if (clients.openWindow) {
        return clients.openWindow(click_action);
      }
    })
  );
});
