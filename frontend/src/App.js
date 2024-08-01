import React from 'react';

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ProfileUpdate from './pages/Account/ProfileUpdate';
import PasswordUpdate from './pages/Account/PasswordUpdate';
import Login from './pages/Account/Login';
import PlantDetail from './pages/Plant/PlantDetail';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/profile-update" element={<ProfileUpdate />} />
        <Route path="/password-update" element={<PasswordUpdate />} />
      </Routes>
    </Router>
  );
};

export default App;
