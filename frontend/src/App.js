import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom';
import React, { useEffect } from 'react';
import useAuthStore from './stores/member';

// private
import PrivateRoute from './components/Account/PrivateRoute';
import PublicRoute from './components/Account/PublicRoute';
// default
import Home from './pages/Main/Home';
import Header from './components/Common/Header';
import Footer from './components/Common/Footer';
// FCM
import { getMessaging, getToken } from 'firebase/messaging';
import { requestForToken, onForegroundMessage } from './firebase';
// Account
import SignUp from './pages/Account/SignUp';
import Login from './pages/Account/Login';
import PasswordFind from './pages/Account/PasswordFind';
import PasswordChange from './pages/Account/PasswordChange';
import ProfileUpdate from './pages/Account/ProfileUpdate';
import PasswordUpdate from './pages/Account/PasswordUpdate';
import Setting from './pages/Account/Setting';
// Plant
import PlantDiaryWrite from './pages/Diary/PlantDiaryWrite';
import PlantDiaryDetail from './pages/Diary/PlantDiaryDetail';
import PlantDetail from './pages/Plant/PlantDetail';
import PlantRegister from './pages/Plant/PlantRegister';
import PlantGuide from './pages/Plant/PlantGuide';
import PlantReport from './pages/Plant/PlantReport';
import ApiTest from './pages/Plant/ApiTest';
// Profile
import UserProfile from './pages/Profile/UserProfile';
import Neighbor from './pages/Profile/Neighbor';
// SNS
import SnsWrite from './pages/Sns/SnsWrite';
import SnsDetail from './pages/Sns/SnsDetail';
import SnsList from './pages/Sns/SnsList';
import CommentDetail from './pages/Sns/CommentDetail';
<<<<<<< HEAD
// Chat
import ChatRoom from './pages/Chat/ChatRoom';
import ChatRooms from './pages/Chat/ChatRoomList';
=======
// Alarm
import Alarm from './pages/Alarm/Alarm';

>>>>>>> de84b74b825daaa5474492a905365efa59138254


// 로그인 후에만 FCM 토큰을 요청하고 저장했다면, 여기는 onForegroundMessage만 설정
if (Notification.permission === 'granted') {
  onForegroundMessage(); // 포그라운드 메시지 리스너 초기화
}



function App() {

  // 앱 실행시 자동로그인 시도
  const autoLogin = useAuthStore((state) => state.autoLogin);

  useEffect(() => {
    autoLogin();
  }, [autoLogin]);


  // useEffect(() => {
  //   const initFCM = async () => {
  //     const token = await requestForToken();
  //     if (token) {
  //       document.cookie = `fcmToken=${token}; path=/; SameSite=Lax`;
  //     }
  //     onForegroundMessage(); // 포그라운드 메시지 리스너 초기화
  //   };

  //   initFCM();
  // }, []);

  return (
    <div className='container'>
      <Router>
        <header className='full-width'>
          <Header />
        </header>
        <main className='content'>
          <Routes>
            {/* Account */}
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<PublicRoute><Login /></PublicRoute>} />
            <Route path="/signup" element={<PublicRoute><SignUp /></PublicRoute>} />
            <Route path="/profileupdate" element={<PrivateRoute><ProfileUpdate /></PrivateRoute>} />
            <Route path="/password/change" element={<PrivateRoute><PasswordChange /></PrivateRoute>} />
            <Route path="/password/find" element={<PublicRoute><PasswordFind /></PublicRoute>} />
            <Route path="/password/update" element={<PasswordUpdate />} />
            <Route path="/setting" element={<PrivateRoute><Setting /></PrivateRoute>} />
            {/* plant */}
            <Route path="/plant/:plantId/:date/write" element={<PrivateRoute><PlantDiaryWrite /></PrivateRoute>} />
            <Route path="/plant/:platId/:date" element={<PlantDiaryDetail />} />
            <Route path="/plant/:plantId" element={<PlantDetail />} />
            <Route path="/plant/:plantId/report" element={<PrivateRoute><PlantReport /></PrivateRoute>} />
            <Route path="/guide/:plantTypeId" element={<PlantGuide />} />
            <Route path="/plant/register" element={<PrivateRoute><PlantRegister /></PrivateRoute>} />
            {/* profile */}
            <Route path="/profile/:searchId" element={<PrivateRoute><UserProfile /></PrivateRoute>} />
            <Route path="/profile/:searchId/neighbor" element={<PrivateRoute><Neighbor /></PrivateRoute>}/>
            {/* sns */}
            <Route path="/sns" element={<SnsList />} />
            <Route path="/sns/write" element={<PrivateRoute><SnsWrite /></PrivateRoute>} />
            <Route path="/sns/:articleId" element={<SnsDetail />} />
            <Route path="/sns/:articleId/comment" element={<CommentDetail />} />
            {/* chat */}
            <Route path="/chat" element={<ChatRooms />} />
            <Route path="/chat/:chatRoomId" element={<ChatRoom />} />
            {/* alarm */}
            <Route path="/alarm" element={<PrivateRoute><Alarm /></PrivateRoute>} />

          </Routes>
        </main>
        <FooterWithCondition />
      </Router>
    </div>
  );
}

// sns 들어가면 댓글작성 footer 출력
const FooterWithCondition = () => {
  const location = useLocation();
  const isSnsDetailPage = location.pathname.startsWith('/sns/');

  return (
    <footer className='full-width'>
      {isSnsDetailPage ? null : <Footer />}
    </footer>
  );
}

export default App;