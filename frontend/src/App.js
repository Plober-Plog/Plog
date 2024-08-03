import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import PlantDiaryWrite from './pages/Diary/PlantDiaryWrite';
import PlantDiaryDetail from './pages/Diary/PlantDiaryDetail';
import PlantDetail from './pages/Plant/PlantDetail';
import SnsWrite from './pages/Sns/SnsWrite';
function App() {
  return (
    <Router>
    <Routes>
      <Route path="/" element={<SnsWrite />} />
      <Route path="/write" element={<PlantDiaryWrite />} />
      <Route path="/diary/:plantDiaryId" element={<PlantDiaryDetail />} />
    </Routes>
    </Router>
  );
}

export default App;
