import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import ProfileUpdate from './pages/Account/ProfileUpdate';
import PasswordUpdate from './pages/Account/PasswordUpdate';
import SignUp from './pages/Account/SignUp';
import Setting from './pages/Account/Setting';
import PlantDiaryWrite from './pages/Diary/PlantDiaryWrite';
import PlantDiaryDetail from './pages/Diary/PlantDiaryDetail';
import PlantDetail from './pages/Plant/PlantDetail';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<SignUp />} />
        <Route path="/write" element={<PlantDiaryWrite />} />
        <Route path="/diary/:plantDiaryId" element={<PlantDiaryDetail />} />
      </Routes>
    </Router>
  );
}

export default App;
