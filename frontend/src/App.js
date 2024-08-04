import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

// Account
import SignUp from './pages/Account/SignUp';
import ProfileUpdate from './pages/Account/ProfileUpdate';
import PasswordUpdate from './pages/Account/PasswordUpdate';
import Setting from './pages/Account/Setting';
// Plant
import PlantDiaryWrite from './pages/Diary/PlantDiaryWrite';
import PlantDiaryDetail from './pages/Diary/PlantDiaryDetail';
import PlantDetail from './pages/Plant/PlantDetail';
import PlantRegister from './pages/Plant/PlantRegister';
import ApiTest from './pages/Plant/ApiTest';
// import Header from './components/Common/Header';

function App() {
  return (
    <div className='container'>
      <header className='full-width'>
        {/* <Header /> */}
      </header>
      <main class='content'>
        <Router>
          <Routes>
            <Route path="/signup" element={<SignUp />} />
            <Route path="/profileupdate" element={<ProfileUpdate />} />
            <Route path="/password/update" element={<PasswordUpdate />} />
            <Route path="/setting" element={<Setting />} />
            <Route path="/write" element={<PlantDiaryWrite />} />
            <Route path="/diary/:plantDiaryId" element={<PlantDiaryDetail />} />
            {/* <Route path="/plant/:plantId" element={<PlantDetail />} /> */}
            <Route path="/plant" element={<PlantDetail />} />
            {/* <Route path="/plant/register/:plantId" element={<PlantDetail />} /> */}
            <Route path="/plant/register" element={<PlantRegister />} />
            {/* API TEST 용 */}
            {/* <Route path="/" element={<ApiTest />} /> */}
          </Routes>
        </Router>
      </main>
    </div>
  );
}

export default App;
