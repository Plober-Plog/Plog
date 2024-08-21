import React, { useEffect, useState } from 'react';
<<<<<<< HEAD
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
=======
import API from '../../apis/api';
import './ProfilePlantTagList.css';

const ProfilePlantTagList = ({ searchId, onFilterUpdate }) => {
  const [plantTags, setPlantTags] = useState([]);
  const [selectedTags, setSelectedTags] = useState([]);
  
  useEffect(() => {
    const fetchPlantTags = async () => {
      try {
        const response = await API.get(`/user/plant-type`, {
          params: { searchId }
        });

        const plantTypeTags = response.data.plantTypes.map(plant => ({
          id: plant.plantTypeId,
          plantName: plant.plantName,
          type: 'plantTypeId'
        }));

        const otherPlantTypeTags = response.data.otherPlantTypes.map(plant => ({
          id: plant.otherPlantTypeId,
          plantName: plant.plantName,
          type: 'otherPlantTypeId'
        }));

        const allPlants = [...plantTypeTags, ...otherPlantTypeTags];
        const uniquePlants = allPlants.reduce((acc, plant) => {
          if (!acc.some(item => item.id === plant.id && item.type === plant.type)) {
>>>>>>> master
            acc.push(plant);
          }
          return acc;
        }, []);

        setPlantTags(uniquePlants);
<<<<<<< HEAD
=======
        // 전체 목록을 초기 상태로 설정
        const initialParams = { searchId, page: 0 };
        const initialResponse = await API.get(`/user/plant`, { params: initialParams });
        onFilterUpdate(initialResponse.data);
>>>>>>> master
      } catch (error) {
        console.error('Error fetching plant tags:', error);
      }
    };

    fetchPlantTags();
  }, [searchId]);

<<<<<<< HEAD
  return (
    <div className="plant-tag-list">
      {plantTags.map((plant) => (
        <button key={plant.plantTypeId} className="plant-tag" style={{ marginRight: '10px' }}>
=======
  const handleTagSelect = async (id, type) => {
    const isSelected = selectedTags.some(tag => tag.id === id && tag.type === type);
    const newSelectedTags = isSelected
      ? selectedTags.filter(tag => !(tag.id === id && tag.type === type))
      : [...selectedTags, { id, type }];

    setSelectedTags(newSelectedTags);

    const params = { searchId, page: 0 };
    const plantTypeIds = newSelectedTags.filter(tag => tag.type === 'plantTypeId').map(tag => tag.id);
    const otherPlantTypeIds = newSelectedTags.filter(tag => tag.type === 'otherPlantTypeId').map(tag => tag.id);

    if (plantTypeIds.length > 0) {
      params.plantTypeId = plantTypeIds.join(',');
    }
    if (otherPlantTypeIds.length > 0) {
      params.otherPlantTypeId = otherPlantTypeIds.join(',');
    }

    try {
      const response = await API.get(`/user/plant`, { params });
      onFilterUpdate(response.data); // 필터된 식물 목록 업데이트
    } catch (error) {
      console.error('Error fetching plants:', error);
    }
  };

  return (
    <div className="plant-tag-list">
      {plantTags.map((plant) => (
        <button 
          key={plant.id}
          className={`plant-tag ${selectedTags.some(tag => tag.id === plant.id && tag.type === plant.type) ? 'active' : ''}`}
          onClick={() => handleTagSelect(plant.id, plant.type)}
        >
>>>>>>> master
          {plant.plantName}
        </button>
      ))}
    </div>
  );
};

export default ProfilePlantTagList;
