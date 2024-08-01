import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import PlantDetail from './pages/Plant/PlantDetail';
import SignUp from './pages/Account/SignUp';

function App() {
  return (
    <div className="App">
      <SignUp />
    </div>

  );
}

export default App;
