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
import './PlantDiaryWrite.css';

const PlantDiaryDetail = () => {
  const location = useLocation();
  const { date, content, isWatered, isFertilized, isRepotted, imgs } = location.state.diaryData;
  const navigate = useNavigate();

  // 임시 데이터
  const weather = '강수량이 많아요';
  const temperature = '온도가 낮아요';
  const humidity = '습도가 높아요';
  const weathercontent = '강수량이 많고 습도가 높으니 어쩌구 하세요'

  const handleEdit = () => {
    navigate('/', { state: { diaryData: location.state.diaryData } });
  };

  return (
    <div className="plant-diary-container">
      <div className="section">
        <h2>{date}</h2>
        <DiaryTodoIcon src={pencilIcon} onClick={handleEdit} />
        <Btn content="X" onClick={() => navigate(-1)} /> {/* 이 부분은 X 를 클릭하면 PlantDetail 페이지로 돌아가야함 */}
      </div>
      <div className="section">
        <ImageSlider imgs={imgs} />
      </div>
      <div className="section">
        {isWatered && (
          <div>
            <DiaryTodoIcon src={waterIcon} /> 물주기 완료!
          </div>
        )}
        {isFertilized && (
          <div>
            <DiaryTodoIcon src={fertilizedIcon} /> 영양제주기 완료!
          </div>
        )}
        {isRepotted && (
          <div>
            <DiaryTodoIcon src={repottedIcon} /> 분갈이 완료!
          </div>
        )}
      </div>
      <div className="section">
        <div className="todo-icons">
          <DiaryTodoIcon src={weatherIcon} />
          <DiaryTodoIcon src={humidityIcon} />
          <DiaryTodoIcon src={temperatureIcon} />
        </div>
        <DiaryWeather
          weather={weather}
          temperature={temperature}
          humidity={humidity}
          content={weathercontent}
        />
      </div>
      <div className="section">
        <DiaryDetailContent detailContent={content}/>
      </div>
    </div>
  );
};


export default PlantDiaryDetail;
