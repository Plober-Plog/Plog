import React from 'react';
<<<<<<< HEAD

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ProfileUpdate from './pages/Account/ProfileUpdate';
import PasswordUpdate from './pages/Account/PasswordUpdate';
import Signup from './pages/Account/SignUp';
import Setting from './pages/Account/Setting';

function App() {
  return (
    <div>
      <Signup />
    </div>
=======
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import PlantDiaryWrite from './pages/Diary/PlantDiaryWrite';
import PlantDiaryDetail from './pages/Diary/PlantDiaryDetail';
import PlantDetail from './pages/Plant/PlantDetail';

function App() {
  return (
    <Router>
    <Routes>
      <Route path="/" element={<PlantDetail />} />
      <Route path="/write" element={<PlantDiaryWrite />} />
      <Route path="/diary/:plantDiaryId" element={<PlantDiaryDetail />} />
    </Routes>
    </Router>
>>>>>>> 4108b2d81af7a8390916b0fcd08c3a914825f973
  );
}

export default App;
