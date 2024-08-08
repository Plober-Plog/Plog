import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import Img from '../../components/Common/Img';
import DiaryDetailContent from '../../components/Diary/DiaryDetailContent';
import API from '../../apis/api';

const PlantGuide = () => {
  const location = useLocation();
  const { plantTypeId } = location.state;

  const [plantData, setPlantData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPlantData = async () => {
      try {
        const response = await API.get(`/user/plant-type/${plantTypeId}`);
        setPlantData(response.data);
        console.log(response.data);
        setLoading(false);
      } catch (error) {
        setLoading(false);
      }
    };

    fetchPlantData();
  }, [plantTypeId]);
  
  if (loading) return <p>로딩중입니당..</p>;

  if (error) return <p>에러났어용!!!!</p>;

  return (
    <div>
      <h2>{plantData.plantName} 키우기</h2>
      <Img src={plantData.profile} alt={`식물이미지`} />
      <DiaryDetailContent detailContent={plantData.guide} />
      <p>물주는 주기: {plantData.waterInterval} 일 🌱</p>
      <p>영양제 주기: {plantData.fertilizeInterval} 일🌱</p>
      <p>분갈이 주기: {plantData.repotInterval} 일🌱</p>
    </div>
  );
};

export default PlantGuide;
