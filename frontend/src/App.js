import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import PlantDiaryWrite from './pages/Diary/PlantDiaryWrite';
import PlantDiaryDetail from './pages/Diary/PlantDiaryDetail';
import PlantDetail from './pages/Plant/PlantDetail';
import PlantRegister from './pages/Plant/PlantRegister';
import ApiTest from './pages/Plant/ApiTest';
import Header from './components/Common/Header';
import UserProfile from './pages/Profile/UserProfile';

function App() {
  return (
    <Router>

      <div className='container'>

        <header className='full-width'>
          <Header />
        </header>

        <main className='content'>
            <Routes>
              {/* <Route path="/plant/:plantId" element={<PlantDetail />} /> */}
              <Route path="/plant" element={<PlantDetail />} />
              <Route path="/plant/register" element={<PlantRegister />} />
              {/* <Route path="/profile/:userId" element={<Profile />}/> */}
              <Route path="/profile" element={<UserProfile />}/>
            </Routes>
        </main>

      </div>

    </Router>
  );
}

export default App;
