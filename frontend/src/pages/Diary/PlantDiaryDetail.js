import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import DiaryTodoIcon from '../../components/Diary/DiaryTodoIcon';
import ImageSlider from '../../components/Common/ImgSlider';
import DiaryWeather from '../../components/Diary/DiaryWeather';
import DiaryDetailContent from '../../components/Diary/DiaryDetailContent';
import Btn from '../../components/Common/Btn';

import pencilIcon from '../../assets/icon/pencil.png'; 
import waterIcon from '../../assets/icon/water.png'; 
import fertilizedIcon from '../../assets/icon/fertilized.png'; 
import repottedIcon from '../../assets/icon/repotted.png'; 
import weatherIcon from '../../assets/icon/weather.png'; 
import humidityIcon from '../../assets/icon/humidity.png'; 
import temperatureIcon from '../../assets/icon/temperature.png'; 

const PlantDiaryDetail = () => {
  const location = useLocation();
  const { date, content, isWatered, isFertilized, isRepotted } = location.state.diaryData;
  const navigate = useNavigate();

  // 임시 데이터
  const weather = '강수량이 많아요';
  const temperature = '온도가 낮아요';
  const humidity = '습도가 높아요';
  const weathercontent = '강수량이 많고 습도가 높으니 어쩌구 하세요'
  const images = [
    { imageId: 1, url: 'https://via.placeholder.com/150', isThumbnail: true },
    { imageId: 2, url: 'https://via.placeholder.com/150', isThumbnail: false },
  ];
  const handleEdit = () => {
    navigate('/', { state: { diaryData: location.state.diaryData } });
  };

  return (
    <div>
      <div>
        {date}
        <DiaryTodoIcon src={pencilIcon} onClick={handleEdit} />
        <Btn content="X" onClick={() => navigate(-1)} /> {/* 이 부분은 X 를 클릭하면 PlantDetail 페이지로 돌아가야함 */}
      </div>
      <ImageSlider images={images} />
      <div>
        {isWatered && <div><DiaryTodoIcon src={waterIcon} /> 물주기 완료!</div>}
        {isFertilized && <div><DiaryTodoIcon src={fertilizedIcon} /> 영양제주기 완료!</div>}
        {isRepotted && <div><DiaryTodoIcon src={repottedIcon} /> 분갈이 완료!</div>}
      </div>
      <div>
        <DiaryTodoIcon src={weatherIcon} />
        <DiaryTodoIcon src={humidityIcon} />
        <DiaryTodoIcon src={temperatureIcon} />
        <DiaryWeather weather={weather} temperature={temperature} humidity={humidity} content={weathercontent} />
      </div>
      <DiaryDetailContent detailContent={content}/>
    </div>
  );
};

export default PlantDiaryDetail;
