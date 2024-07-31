import React from 'react';
import PlantDiaryWrite from './pages/Diary/PlantDiaryWrite';
import PlantDiaryDetail from './pages/Diary/PlantDiaryDetail';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

function App() {
  return (
    // <div className="App">
    //   <PlantDiaryWrite />
    // </div>
    <Router>
    <Routes>
      <Route path="/" element={<PlantDiaryWrite />} />
      <Route path="/diary/:plantDiaryId" element={<PlantDiaryDetail />} />
    </Routes>
    </Router>
  );
}

export default App;
