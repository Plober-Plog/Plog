import React, { useState, useEffect } from 'react';
import Img from '../../components/Common/Img';
import DiaryDetailContent from '../../components/Diary/DiaryDetailContent';
import axios from 'axios';

const PlantGuide = () => {
  const [plantData, setPlantData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPlantData = async () => {
      const URI = 'https://i11b308.p.ssafy.io//api/user/plant-type/2';
      try {
        const response = await axios.get(URI);
        setPlantData(response.data);
        console.log(response.data);
        setLoading(false);
      } catch (err) {
        setError(err);
        setLoading(false);
      }
    };

    fetchPlantData();
  }, []);
  
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
