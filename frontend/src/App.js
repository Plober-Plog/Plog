import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

// default
import Home from './pages/Main/Home';
import Header from './components/Common/Header';
import Footer from './components/Common/Footer';

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

// Profile
import UserProfile from './pages/Profile/UserProfile';
import Neighbor from './pages/Profile/Neighbor';

// SNS
import SnsWrite from './pages/Sns/SnsWrite';
import SnsDetail from './pages/Sns/SnsDetail';
import SnsList from './pages/Sns/SnsList';

function App() {
  return (
    <div className='container'>
      <Router>
        <header className='full-width'>
          <Header />
        </header>
        <main className='content'>

            <Routes>
              {/* Account */}
              <Route path="/login" element={<Login />} />
              <Route path="/signup" element={<SignUp />} />
              <Route path="/profileupdate" element={<ProfileUpdate />} />
              <Route path="/password/change" element={<PasswordChange />} />
              <Route path="/password/find" element={<PasswordFind />} />
              <Route path="/password/update" element={<PasswordUpdate />} />
              <Route path="/setting" element={<Setting />} />
              {/* plant */}
              <Route path="/plant/:plantId/:date/write" element={<PlantDiaryWrite />} />
              <Route path="/plant/:platId/:date" element={<PlantDiaryDetail />} />
              <Route path="/plant/:plantId" element={<PlantDetail />} />
              <Route path="/guide/:plantTypeId" element={<PlantGuide />} />
              <Route path="/plant/register" element={<PlantRegister />} />
              {/* profile */}
              <Route path="/profile/:searchId" element={<UserProfile />} />
              <Route path="/profile/:searchId/neighbor" element={<Neighbor />}/>
              {/* sns */}
              <Route path="/sns" element={<SnsList />} />
              <Route path="/sns/write" element={<SnsWrite />} />
              <Route path="/sns/:articleId" element={<SnsDetail />} />
            </Routes>
        </main>
        <footer className='full-width'>
          <Footer />
        </footer>
      </Router>
    </div>
  );
};

export default App;
