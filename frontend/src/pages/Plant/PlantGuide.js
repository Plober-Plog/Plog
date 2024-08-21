import React, { useState, useEffect } from 'react';
<<<<<<< HEAD
import Img from '../../components/Common/Img';
import DiaryDetailContent from '../../components/Diary/DiaryDetailContent';
import axios from 'axios';

const PlantGuide = () => {
=======
import { useLocation } from 'react-router-dom';
import Img from '../../components/Common/Img';
import DiaryDetailContent from '../../components/Diary/DiaryDetailContent';
import API from '../../apis/api';
import './PlantGuide.css';  // CSS 파일을 import

const PlantGuide = () => {
  const location = useLocation();
  const { plantTypeId } = location.state;

>>>>>>> master
  const [plantData, setPlantData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPlantData = async () => {
<<<<<<< HEAD
      const URI = 'https://i11b308.p.ssafy.io//api/user/plant-type/2';
      try {
        const response = await axios.get(URI);
        setPlantData(response.data);
        console.log(response.data);
        setLoading(false);
      } catch (err) {
        setError(err);
        setLoading(false);
=======
      try {
        const response = await API.get(`/user/plant-type/${plantTypeId}`);
        setPlantData(response.data);
        setLoading(false);
      } catch (error) {
        setLoading(false);
        setError(error);
>>>>>>> master
      }
    };

    fetchPlantData();
<<<<<<< HEAD
  }, []);
=======
  }, [plantTypeId]);
>>>>>>> master
  
  if (loading) return <p>로딩중입니당..</p>;

  if (error) return <p>에러났어용!!!!</p>;

  return (
    <div>
<<<<<<< HEAD
      <h2>{plantData.plantName} 키우기</h2>
      <Img src={plantData.profile} alt={`식물이미지`} />
      <DiaryDetailContent detailContent={plantData.guide} />
      <p>물주는 주기: {plantData.waterInterval} 일 🌱</p>
      <p>영양제 주기: {plantData.fertilizeInterval} 일🌱</p>
      <p>분갈이 주기: {plantData.repotInterval} 일🌱</p>
=======
      <div>
        <h2 className='cardlist-subtitle mt-5 m-4'>{plantData.plantName} 키우기</h2>
      </div>
      <div>
        <Img src={plantData.profile} alt={`식물이미지`} />
      </div>
      <div className="plant-guide-container plant-guide-info">
        <DiaryDetailContent detailContent={plantData.guide} />
        <br/>
        <p>물주는 주기: {plantData.waterInterval} 일 🌱</p>
        <p>영양제 주기: {plantData.fertilizeInterval} 일 🌱</p>
        <p>분갈이 주기: {plantData.repotInterval} 일 🌱</p>
      </div>
>>>>>>> master
    </div>
  );
};

export default PlantGuide;
