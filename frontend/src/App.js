import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ProfileUpdate from './pages/Account/ProfileUpdate';
import PasswordUpdate from './pages/Account/PasswordUpdate';
import Setting from './pages/Account/Setting';
import PlantDetail from './pages/Plant/PlantDetail';

function App() {
  return (
    <div>
      <Setting />
    </div>
  );
}

export default App;
