import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import PlantDiaryWrite from './pages/Diary/PlantDiaryWrite';
import PlantDiaryDetail from './pages/Diary/PlantDiaryDetail';
import PlantDetail from './pages/Plant/PlantDetail';
import PlantRegister from './pages/Plant/PlantRegister';
import ApiTest from './pages/Plant/ApiTest';

function App() {
  return (
    <Router>
      <Routes>
        {/* <Route path="/plant/:plantId" element={<PlantDetail />} /> */}
        <Route path="/plant" element={<PlantDetail />} />
        {/* <Route path="/plant/register/:plantId" element={<PlantDetail />} /> */}
        <Route path="/plant/register" element={<PlantRegister />} />
        {/* API TEST 용 */}
        <Route path="/" element={<ApiTest />} />
      </Routes>
    </Router>
  );
}

export default App;
