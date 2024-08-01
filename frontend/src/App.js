import React from 'react';

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ProfileUpdate from './pages/Account/ProfileUpdate';
import PasswordUpdate from './pages/Account/PasswordUpdate';
import Setting from './pages/Account/Setting';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import PlantDetail from './pages/Plant/PlantDetail';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Setting />} />
        <Route path="/profile-update" element={<ProfileUpdate />} />
        <Route path="/password-update" element={<PasswordUpdate />} />
        <Route path="/" element={<PlantDetail />} />
      </Routes>
    </Router>
  );
}

export default App;
