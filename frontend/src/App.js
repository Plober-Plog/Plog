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
  );
}

export default App;
