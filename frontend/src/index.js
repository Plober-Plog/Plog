import React from 'react';
import ReactDOM from 'react-dom/client';
// import './index.css';
import './styles/index.css';
import App from './App';
import * as serviceWorkerRegistration from './serviceWorkerRegistration';
import reportWebVitals from './reportWebVitals';
// import { requestForToken, onMessageListener } from './firebase'; // FCM 관련 코드 추가

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <App />
  // <React.StrictMode>
  // </React.StrictMode>
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://cra.link/PWA
// serviceWorkerRegistration.unregister();
serviceWorkerRegistration.register();

// FCM 토큰 요청 추가
// requestForToken();

// FCM 메시지 리스너 추가
// onMessageListener().then(payload => {
//   console.log('Received foreground message: ', payload);
//   // 여기에 알림을 표시하는 코드를 추가할 수 있습니다.
// }).catch(err => console.log('failed: ', err));

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
