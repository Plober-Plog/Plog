import React from 'react';

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ProfileUpdate from './pages/Account/ProfileUpdate';
import PasswordUpdate from './pages/Account/PasswordUpdate';
import Signup from './pages/Account/SignUp';
import Setting from './pages/Account/Setting';

function App() {
  return (
    <div>
      <Login />
    </div>
  );
}

export default App;
