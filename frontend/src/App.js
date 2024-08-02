import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import PlantDetail from './pages/Plant/PlantDetail';
import PlantRegister from './pages/Plant/PlantRegister';

function App() {
  return (
    <Router>
      <Routes>
        {/* <Route path="/plant/:plantId" element={<PlantDetail />} /> */}
        <Route path="/plant" element={<PlantDetail />} />
        {/* <Route path="/plant/register/:plantId" element={<PlantDetail />} /> */}
        <Route path="/plant/register" element={<PlantRegister />} />
      </Routes>
    </Router>
  );
}

export default App;
