import React, { useState, useEffect, useRef } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import API from '../../apis/api';
import DiaryTodoIcon from '../../components/Diary/DiaryTodoIcon';
import ImageSlider from '../../components/Common/ImgSlider';
import DiaryWeather from '../../components/Diary/DiaryWeather';
import DiaryDetailContent from '../../components/Diary/DiaryDetailContent';
import Btn from '../../components/Common/Btn';
// import html2canvas from 'html2canvas';

import pencilIcon from '../../assets/icon/pencil.png'; 
import waterFillIcon from '../../assets/icon/water-select.png'; 
import fertilizedFillIcon from '../../assets/icon/fertilized-select.png'; 
import repottedFillIcon from '../../assets/icon/repotted-select.png';
import weatherIcon from '../../assets/icon/weather.png'; 
import humidityIcon from '../../assets/icon/humidity.png'; 
import temperatureIcon from '../../assets/icon/temperature.png'; 
import './PlantDiaryDetail.css';

const PlantDiaryDetail = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const reportRef = useRef(null);

  const { plantId, date } = location.state;
  const [plantCheck, setPlantCheck] = useState(null);
  const [plantDiary, setPlantDiary] = useState(null);

  // console.log(location);

  // 일지 디테일 불러오기
  useEffect(() => {
    const getPlantDetailDiary = async () => {
      try {
        console.log(plantId, date);
        const response = await API.get(`/user/plant/${plantId}`, {
          params: { date }
        });

        const data = response.data;
        console.log(data);
        setPlantCheck(data.plantCheck || {});
        setPlantDiary(data.plantDiary || {});

      } catch (error) {
        console.error('작성된 일지 확인 에러:', error);
      }
    };

    getPlantDetailDiary();
  }, [plantId, date]);

  if (!plantCheck || !plantDiary) {
    return <div>Loading...</div>;
  }

  const isWatered = plantCheck.isWatered || false;
  const isFertilized = plantCheck.isFertilized || false;
  const isRepotted = plantCheck.isRepotted || false;

  const weather = plantDiary.weather || 1;
  const temperature = plantDiary.temperature || 0.0;
  const humidity = plantDiary.humidity || 1;
  const content = plantDiary.content || '작성된 일지 내용이 없습니다.';
  const images = plantDiary.images || [];
  const plantDiaryId = plantDiary.plantDiaryId;

  const handleEdit = () => {
    navigate(`/plant/${plantId}/${date}/write`, {
      state: {
        diaryData: {
          plantDiaryId,
          date,
          content,
          weather,
          temperature,
          humidity,
          isWatered,
          isFertilized,
          isRepotted,
          imgs: images.map(img => ({ url: img.url })),
          isEditImage: false,
        },
        plantData: {
          plantId, 
          isWatered,
          isFertilized,
          isRepotted,
          checkDate: date,
        },
        plantId: plantId,
        date: date,
      },
    });
  };


  const handleDelete = async () => {
    try {
      const diaryResponse = await API.delete(`/user/diary/${plantDiaryId}`);

      if (diaryResponse.status !== 200) {
        throw new Error('일지 삭제에 실패했습니다.');
      }
      
      const checkResponse = await API.delete(`/user/plant/${plantId}/check`, {
        params: { checkDate: date }
      });
  
      if (checkResponse.status !== 200) {
        throw new Error('식물 관리 기록 삭제에 실패했습니다.');
      }

      alert('일지가 삭제되었습니다.');
      navigate(`/plant/${plantId}`);
    } catch (error) {
      console.error('Error:', error);
      alert('일지 삭제 중 오류가 발생했습니다.');
    }
  };

  const weatherContent = `날씨는 ${weather}이고 온도는 ${temperature}'C 이며 습도는 ${humidity}입니다.`;

  

  return (
    <div className="plant-diary-detail-container" ref={reportRef}>
      <div className="plant-diary-detail-header m-5">
        <h2 className="plant-diary-detail-date">{date}</h2>
        <button className="plant-diary-detail-icon" onClick={handleEdit}>
          <img src={pencilIcon} alt="Edit" />
        </button>
        <button className="plant-diary-detail-close-btn" onClick={() => navigate(`/plant/${plantId}`)}>
          X
        </button>
      </div>
      <div className='mb-3'>
        <ImageSlider imgs={images.map(img => img)} />
      </div>
      <div className='plant-diary-detail-todo-container'>
        {(!isWatered && !isFertilized && !isRepotted) ? (
          <div>식물관리내역이 없습니다.</div>
        ) : (
          <div>
            {isWatered && (
              <div className="plant-diary-detail-todo-item">
                <DiaryTodoIcon src={waterFillIcon} className="plant-diary-detail-todo-icon" />
                <span className="plant-diary-detail-todo-text">물주기 완료!</span>
              </div>
            )}
            {isFertilized && (
              <div className="plant-diary-detail-todo-item">
                <DiaryTodoIcon src={fertilizedFillIcon} className="plant-diary-detail-todo-icon" />
                <span className="plant-diary-detail-todo-text">영양제주기 완료!</span>
              </div>
            )}
            {isRepotted && (
              <div className="plant-diary-detail-todo-item">
                <DiaryTodoIcon src={repottedFillIcon} className="plant-diary-detail-todo-icon" />
                <span className="plant-diary-detail-todo-text">분갈이 완료!</span>
              </div>
            )}
          </div>
        )}
      </div>
      <div className="plant-diary-detail-weather-container">
        <div className="plant-diary-detail-weather-icons">
          <div className="plant-diary-detail-weather-item">
            <DiaryTodoIcon src={weatherIcon} className="plant-diary-detail-weather-icon" />
          </div>
          <div className="plant-diary-detail-weather-item">
            <DiaryTodoIcon src={humidityIcon} className="plant-diary-detail-weather-icon" />
          </div>
          <div className="plant-diary-detail-weather-item">
            <DiaryTodoIcon src={temperatureIcon} className="plant-diary-detail-weather-icon" />
          </div>
        </div>
        <DiaryWeather content={weatherContent} />
      </div>
      <div className="plant-diary-detail-weather-container">
        <DiaryDetailContent detailContent={content} />
      </div>
      <div className='plant-diary-detail-btn'>
        <Btn content="삭제하기" onClick={handleDelete} /> 
        {/* <Btn content="SNS 업로드" onClick={handleCapture} />  */}
      </div>
    </div>
  );
};

export default PlantDiaryDetail;
