import React from 'react';
<<<<<<< HEAD
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ProfileUpdate from './pages/Account/ProfileUpdate';
import PasswordUpdate from './pages/Account/PasswordUpdate';
import Setting from './pages/Account/Setting';

=======
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import PlantDetail from './pages/Plant/PlantDetail';
>>>>>>> a0d600fa7be1e32f47cb36e0870870b21fa5fa71
import PlantDiaryWrite from './pages/Diary/PlantDiaryWrite';
import PlantDiaryDetail from './pages/Diary/PlantDiaryDetail';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

function App() {
  return (
    <Router>
      <Routes>
<<<<<<< HEAD
        <Route path="/" element={<Setting />} />
        <Route path="/profile-update" element={<ProfileUpdate />} />
        <Route path="/password-update" element={<PasswordUpdate />} />
=======
        <Route path="/" element={<PlantDetail />} />
>>>>>>> a0d600fa7be1e32f47cb36e0870870b21fa5fa71
      </Routes>
    </Router>
    // <div className="App">
    //   <PlantDiaryWrite />
    // </div>
    <Router>
    <Routes>
      <Route path="/" element={<PlantDiaryWrite />} />
      <Route path="/diary/:plantDiaryId" element={<PlantDiaryDetail />} />
    </Routes>
    </Router>
  );
}

export default App;
