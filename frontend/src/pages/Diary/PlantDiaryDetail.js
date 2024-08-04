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

const PlantDiaryDetail = ({ currentDate, plantId }) => {
  const location = useLocation();
  // TODO 여기도 plantDiaryId에 저장된 정보받아올듯
  const { plantDiaryId, date, content, weather, temperature, humidity, isWatered, isFertilized, isRepotted, imgs } = location.state; 
  const navigate = useNavigate();

  // 임시 데이터
  const weathercontent = '강수량이 많고 습도가 높으니 어쩌구 하세요'

  const handleEdit = () => {
    navigate('/write', { state: { diaryData: location.state } });
  };
  
  const handleSNSUpload = () => {
    navigate('/sns', { state: { diaryData: location.state } });
  };

  const handleDelete = async () => {
    try {
      const response = await fetch(`/api/user/diary/${plantDiaryId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error('일지 삭제에 실패했습니다.');
      }

      alert('일지가 삭제되었습니다.');
      navigate('/'); 
    } catch (error) {
      console.error('Error:', error);
      alert('일지 삭제 중 오류가 발생했습니다.');
    }
  };

  return (
    <div className="plant-diary-container">
      <div className="section">
        <h2>{date}</h2>
        <DiaryTodoIcon src={pencilIcon} onClick={handleEdit} />
        <Btn content="X" onClick={() => navigate('/')} /> {/* 이 부분은 X 를 클릭하면 PlantDetail 페이지로 돌아가야함 */}
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
      <div>
        <Btn content="삭제하기" onClick={handleDelete}/> 
        <Btn content="SNS 업로드" onClick={handleSNSUpload}/> 
      </div>
    </div>
  );
};


export default PlantDiaryDetail;
