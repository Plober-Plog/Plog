import React, { useState } from 'react';
import DateDisplay from '../../components/Common/DateDisplay';
import pencilIcon from '../../assets/icon/pencil.png'; 
import DiaryTodoIcon from '../../components/Diary/DiaryTodoIcon';
import ImageSlider from '../../components/Common/ImgSlider';
import DiaryWeather from '../../components/Diary/DiaryWeather';
import DiaryDetailContent from '../../components/Diary/DiaryDetailContent';

import waterIcon from '../../assets/icon/water.png'; 
import fertilizedIcon from '../../assets/icon/fertilized.png'; 
import weatherIcon from '../../assets/icon/weather.png'; 
import humidityIcon from '../../assets/icon/humidity.png'; 
import temperatureIcon from '../../assets/icon/temperature.png'; 


const PlantDiaryDetail = () => {
  const [date, setDate] = useState(new Date().toISOString().slice(0, 10)); 
  // 임시 데이터
  const weather = '강수량이 많아요';
  const temperature = '온도가 낮아요';
  const humidity = '습도가 높아요';
  const content = '강수량이 많고 습도가 높으니 어쩌구 하세요';
  const images = [
    { imageId: 1, url: 'https://via.placeholder.com/150', isThumbnail: true },
    { imageId: 2, url: 'https://via.placeholder.com/150', isThumbnail: false },
  ];

  return (
    <div>
      <div>
       <DateDisplay date={date} />
       <DiaryTodoIcon src={pencilIcon} />
      </div>
      <ImageSlider images={images} />
      <div>
       <DiaryTodoIcon src={waterIcon} /> 물주기 완료! 
       <DiaryTodoIcon src={fertilizedIcon} /> 영양제주기 완료!
      </div>
      <div>
       <DiaryTodoIcon src={weatherIcon} />
       <DiaryTodoIcon src={humidityIcon} />
       <DiaryTodoIcon src={temperatureIcon} />
       <DiaryWeather weather={weather} temperature={temperature} humidity={humidity} content={content} />
      </div>
      <DiaryDetailContent detailContent={content}/>
    </div>
    
  );
};

export default PlantDiaryDetail;