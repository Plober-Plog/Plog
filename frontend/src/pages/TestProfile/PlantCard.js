// src/pages/TestProfile/PlantCard.js
import React from 'react';

const PlantCard = ({ plantName }) => {
  return (
    <div className="plant-card">
      <div className="plant-image-placeholder"></div>
      <div className="plant-info">
        <h3>{plantName}</h3>
        <p>몬스테라</p>
      </div>
    </div>
  );
};

export default PlantCard;
