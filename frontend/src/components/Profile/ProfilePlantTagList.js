import React, { useEffect, useState } from 'react';
import axios from 'axios';

const ProfilePlantTagList = ({ searchId }) => {
  const [plantTags, setPlantTags] = useState([]);

  useEffect(() => {
    const fetchPlantTags = async () => {
      try {
        const response = await axios.get(`https://i11b308.p.ssafy.io/api/user/plant-type`, {
          params: { searchId }
        });

        const allPlants = [...response.data.plantTypes, ...response.data.otherPlantTypes];
        const uniquePlants = allPlants.reduce((acc, plant) => {
          if (!acc.some(item => item.plantTypeId === plant.plantTypeId)) {
            acc.push(plant);
          }
          return acc;
        }, []);

        setPlantTags(uniquePlants);
      } catch (error) {
        console.error('Error fetching plant tags:', error);
      }
    };

    fetchPlantTags();
  }, [searchId]);

  return (
    <div className="plant-tag-list">
      {plantTags.map((plant) => (
        <button key={plant.plantTypeId} className="plant-tag" style={{ marginRight: '10px' }}>
          {plant.plantName}
        </button>
      ))}
    </div>
  );
};

export default ProfilePlantTagList;
