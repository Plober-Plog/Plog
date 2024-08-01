import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import PlantDetail from './pages/Plant/PlantDetail';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<PlantDetail />} />
        {/* <Route path="/plant/:plantId" element={<PlantDetail />} /> */}
      </Routes>
    </Router>
  );
}

export default App;
