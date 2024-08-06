// src/pages/TestProfile/PlantList.js
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import PlantCard from './PlantCard';

const PlantList = ({ searchId }) => {
  const [plantTypes, setPlantTypes] = useState([]);
  const [otherPlantTypes, setOtherPlantTypes] = useState([]);

  useEffect(() => {
    const fetchPlantTypes = async () => {
      try {
        const response = await axios.get(`https://i11b308.p.ssafy.io/api/user/plant-type`, {
          params: { searchId }
        });
        setPlantTypes(response.data.plantTypes);
        setOtherPlantTypes(response.data.otherPlantTypes);
      } catch (error) {
        console.error('Error fetching plant types:', error);
      }
    };

    fetchPlantTypes();
  }, [searchId]);

  return (
    <div className="plant-list">
      {plantTypes.map((plant) => (
        <PlantCard key={plant.plantTypeId} plantName={plant.plantName} />
      ))}
      {otherPlantTypes.map((plant, index) => (
        <PlantCard key={`other-${index}`} plantName={plant.plantName} />
      ))}
    </div>
  );
};

export default PlantList;
