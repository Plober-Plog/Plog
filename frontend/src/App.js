import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import PlantDetail from './pages/Plant/PlantDetail';
import Login from './pages/Account/Login'

function App() {
  return (
    <div className="App">
      <Login />
    </div>

  );
}

export default App;
