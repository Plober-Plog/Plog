import React, { useState, useEffect } from 'react';
import axios from 'axios';
// import API from '../path/to/api';
import { useLocation, useNavigate } from 'react-router-dom';
import DateDisplay from '../../components/Common/DateDisplay';
import Btn from '../../components/Common/Btn';
import Content from '../../components/Common/Content';
import WriterInfo from '../../components/Common/WriterInfo';
import DiaryTodoIcon from '../../components/Diary/DiaryTodoIcon';
import ImgUpload from '../../components/Common/ImgUpload';
import DiaryWeather from '../../components/Diary/DiaryWeather';

import weatherIcon from '../../assets/icon/weather.png'; 
import humidityIcon from '../../assets/icon/humidity.png'; 
import temperatureIcon from '../../assets/icon/temperature.png'; 
import defaultImg from '../../assets/icon/default.png';
import cameraIcon from '../../assets/icon/camera.png';
import waterIcon from '../../assets/icon/water.png'; 
import fertilizedIcon from '../../assets/icon/fertilized.png'; 
import repottedIcon from '../../assets/icon/repotted.png'; 

import './PlantDiaryWrite.css';

//TODO [차유림] 현재는 location 받아오는 게 없어서 오류가 남 우선 주석처리했음
const PlantDiaryWrite = ({currentDate = new Date(), plantId=1 }) => {  
  // const location = useLocation();
  const navigate = useNavigate();
  // const { plantId, date: selectedDate } = location.state;

  const URI = 'https://i11b308.p.ssafy.io/api'
  const [content, setContent] = useState('');
  // const [date, setDate] = useState(selectedDate); 
  const [date, setDate] = useState(new Date(currentDate));
  const [isWatered, setIsWatered] = useState(false);
  const [isFertilized, setIsFertilized] = useState(false);
  const [isRepotted, setIsRepotted] = useState(false);
  const [weather, setIsWeather] = useState('');
  const [humidity, setIsHumidity] = useState('');
  const [temperature, setIsTemperaturer] = useState('');
  const [imgs, setImgs] = useState([]);
  const [plantDiaryId, setPlantDiaryId] = useState(null); //현재 날짜에 이미 작성된 일지가 있을 경우 해당 일지의 ID를 저장
  const [isEditMode, setIsEditMode] = useState(false);


  // 임시 데이터 
  const writerInfoData = {
    profile: defaultImg, 
    nickname: '조이',
    plantTypeId: '몬스테라'
  };

  // 해당 날짜에 작성된 식물 일지 기록 및 관리 기록 확인 
  const fetchDiaryAndCheck = async (plantId, date) => {
    try {
      const response = await axios.get(`${URI}/user/plant/${plantId}`, {
        params: { date },
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoicGxvZy5jb20iLCJleHAiOjE3MjM5MTIyNjEsImlhdCI6MTcyMjcwMjY2MX0.wp3fqP8MHxSy4i-CUZUHnt85iRjS0cksuhu4bbtvhzw`,
        },
      });
      console.log(response);
      return response.data;
    } catch (error) {
      console.error('작성된 일지 확인 에러:', error);
      return null;
    }
  };

 // 컴포넌트 로딩 및 날짜 변경 시 일지 조회
 useEffect(() => {
  const getDiaryAndPlantCheck = async () => {
    try {
      const data = await fetchDiaryAndCheck(plantId, date);
      if (data) {
        setIsEditMode(true);
        if (data.plantDiary) {
          const { plantDiary } = data;
          setPlantDiaryId(plantDiary.plantId);
          setContent(plantDiary.content);
          setImgs(plantDiary.images.map(img => ({ 
            url: img.url, 
            id: img.imageId,
            isThumbnail: img.isThumbnail, 
           })));
          setIsWeather(plantDiary.weather);
          setIsHumidity(plantDiary.humidity);
          setIsTemperaturer(plantDiary.temperature);
        } else {
          // const { plantDiary } = data;
          setPlantDiaryId(null);
          setContent('');
          setImgs([]);
          // setIsWeather(plantDiary.weather);
          // setIsHumidity(plantDiary.humidity);
          // setIsTemperaturer(plantDiary.temperature);
        }
        if (data.plantCheck) {
          const { plantCheck } = data;
          setIsWatered(plantCheck.watered);
          setIsFertilized(plantCheck.fertilized);
          setIsRepotted(plantCheck.repotted);
        } else {
          setIsWatered(false);
          setIsFertilized(false);
          setIsRepotted(false);
        }
      } else {
        // TODO [차유림]아직 날씨 데이터가 없기 때문에 주석처리 
        // const { plantDiary } = data;
        // setIsEditMode(false);
        // setIsWeather(plantDiary.weather);
        // setIsHumidity(plantDiary.humidity);
        // setIsTemperaturer(plantDiary.temperature);
        console.log('새로운 일지를 작성');
      }
    } catch (error) {
      console.error('일지 데이터를 불러오는 중 오류 발생:', error);
    }
  };
  getDiaryAndPlantCheck();
}, [date, plantId]);

  // 정보 입력
  const handleImageUpload = (event) => {
    const files = Array.from(event.target.files); // 파일 입력에서 파일 배열을 만들기
    if (files.length + imgs.length > 5) { // 이미지가 5장을 초과하면 경고하고 반환
      alert('최대 5장까지 업로드할 수 있습니다.');
      return;
    }
    const newImgs = files.map((file, index) => ({
      file,
      url: URL.createObjectURL(file), // 파일 객체로부터 미리보기 URL을 생성
      isThumbnail: imgs.length === 0 && index === 0
    }));

    setImgs(prevImgs => {
      if (prevImgs.length === 0 && newImgs.length > 0) { //길이가 0이고 새로운 이미지를 넣었을떄 길이가 0 이상이되면 
        newImgs[0].isThumbnail = true; // 우선 대표사진으로 설정해줌
      }
      return [...prevImgs, ...newImgs];
    });
  };

  const handleDeleteImage = (index) => {
    setImgs(prevImgs => {
      // 선택한 인덱스의 이미지를 제외한 새로운 배열 생성
      const updatedImgs = prevImgs.filter((_, i) => i !== index);
      // 남은 이미지가 존재하며, 그 중에 썸네일 이미지가 없는 경우
      if (updatedImgs.length > 0 && !updatedImgs.some(img => img.isThumbnail)) { // 우선 대표사진으로 설정해줌
        updatedImgs[0].isThumbnail = true;
      }
      return updatedImgs;
    });
  };

  const handleSetThumbnail = (index) => {
    setImgs(prevImgs => prevImgs.map((img, i) => ({
      ...img,
      isThumbnail: i === index
    })));
  };

  const toggleWatered = () => {
    const newState = !isWatered;
    setIsWatered(newState);
    console.log('Watered:', newState);
  };

  const toggleFertilized = () => {
    const newState = !isFertilized;
    setIsFertilized(newState);
    console.log('Fertilized:', newState);
  };

  const toggleRepotted = () => {
    const newState = !isRepotted;
    setIsRepotted(newState);
    console.log('Repotted:', newState);
  };

  // TODO 아마도 이 부분은 모든 페이지에 적용될것임 지금 날씨 API 가 안되기 때문에 임시로 이렇게 작성 
  const weatherContent = `날씨는 ${weather}단계이고 온도는 ${temperature}도씨 이며 습도는 ${humidity}단계입니다. 그러니 어쩌구 하세요.`;
  
  // 저장 버튼 클릭
  const handleSave = async () => {
    const formattedDate = date instanceof Date ? date.toISOString().split('T')[0] : new Date(date).toISOString().split('T')[0];

    const diaryData = {
      plantId,
      weather,
      temperature,
      humidity,
      content,
      images: imgs.map((img) => ({
        url: img.url,
        isThumbnail: img.isThumbnail
      })),
      recordDate: formattedDate,
    };

    const plantData = {
      isWatered,
      isFertilized,
      isRepotted,
      checkDate: formattedDate,
    }

    // 이미지 정보를 배열로 전달해주기 위한 ...
    const formData = new FormData();
    formData.append('diaryData', new Blob([JSON.stringify(diaryData)], { type: 'application/json' }));

    imgs.forEach((img, index) => {
      formData.append('images', img.file);
      formData.append('thumbnails', JSON.stringify({ url: img.url, isThumbnail: img.isThumbnail || index === 0 }));
    });

    // 일지작성요청 2
    try {
      const diaryWriteResponse = await axios({
        method: plantDiaryId ? 'PATCH' : 'POST',
        url: plantDiaryId ? `${URI}/user/diary/${plantDiaryId}` : `${URI}/user/diary`,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoicGxvZy5jb20iLCJleHAiOjE3MjM5MTIyNjEsImlhdCI6MTcyMjcwMjY2MX0.wp3fqP8MHxSy4i-CUZUHnt85iRjS0cksuhu4bbtvhzw`,
        },
        data: formData,
      });

      if (diaryWriteResponse.status !== 200) {
        throw new Error('일지 저장에 실패했습니다.');
      }

      const plantCheckResponse = await axios({
        method: plantDiaryId ? 'PATCH' : 'POST',
        url: `${URI}/user/plant/${plantId}/check`,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoicGxvZy5jb20iLCJleHAiOjE3MjM5MTIyNjEsImlhdCI6MTcyMjcwMjY2MX0.wp3fqP8MHxSy4i-CUZUHnt85iRjS0cksuhu4bbtvhzw`,
        },
        data: plantData,
      });

      if (plantCheckResponse.status !== 200) {
        throw new Error('식물 정보 저장에 실패했습니다.');
      }

      navigate(`/plant/${plantId}/${formattedDate}`, {
        state: {
          plantDiaryId,
          date: formattedDate,
          content,
          weather,
          temperature,
          humidity,
          isWatered,
          isFertilized,
          isRepotted,
          imgs,
        },
      });
    } catch (error) {
      console.error('Error:', error);
      alert(error.message);
    }
  };

  // 글 쓰기 페이지에서 날짜를 바꿨을 때 useEffect 부터 다시 실행되도록 함 
  const handleDateChange = async (newDate) => {
    const data = await fetchDiaryAndCheck(plantId, newDate);
    if (data && (data.plantDiary || data.plantCheck)) {
      if (window.confirm('이미 작성된 일지가 있습니다. 확인하시겠습니까?')) {
        setDate(newDate);
        setIsEditMode(true);
        if (data.plantDiary) {
          const { plantDiary } = data;
          setPlantDiaryId(plantDiary.plantId);
          setContent(plantDiary.content);
          setImgs(plantDiary.images.map(img => ({
            url: img.url,
            id: img.imageId,
            isThumbnail: img.isThumbnail,
          })));
          setIsWeather(plantDiary.weather);
          setIsHumidity(plantDiary.humidity);
          setIsTemperaturer(plantDiary.temperature);
        } else {
          setPlantDiaryId(null);
          setContent('');
          setImgs([]);          
        }
        if (data.plantCheck) {
          const { plantCheck } = data;
          setIsWatered(plantCheck.watered);
          setIsFertilized(plantCheck.fertilized);
          setIsRepotted(plantCheck.repotted);
        } else {
          setIsWatered(false);
          setIsFertilized(false);
          setIsRepotted(false);         
        }
      }
    } else {
      setDate(newDate);
      setIsEditMode(false);
      
    }
  };

  return (
    <div className="plant-diary-container">
      <div className="section">
        <DateDisplay date={date} setDate={handleDateChange} />
      </div>
      <div className="section">
        <WriterInfo data={writerInfoData} type="plant" />
      </div>
      <div className="section">
        <h2>사진 첨부하기</h2>
        <ImgUpload 
          cameraIcon={cameraIcon}
          imgs={imgs} 
          handleImageUpload={handleImageUpload} 
          handleDeleteImage={handleDeleteImage} 
          handleSetThumbnail={handleSetThumbnail}
        />
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
          content={weatherContent}
        />
      </div>
      <div className="section">
        <h2>오늘 한 일</h2>
        <div className="todo-icons">
          <DiaryTodoIcon src={waterIcon} active={isWatered} onClick={toggleWatered} />
          <DiaryTodoIcon src={fertilizedIcon} active={isFertilized} onClick={toggleFertilized} />
          <DiaryTodoIcon src={repottedIcon} active={isRepotted} onClick={toggleRepotted} />
        </div>
      </div>
      <div className="section">
        <h2>일지 작성</h2>
        <Content content={content} setContent={setContent} />
      </div>
      <div>
        <Btn content={isEditMode ? "수정하기" : "작성하기"} onClick={handleSave} />
      </div>
    </div>
  );
};

export default PlantDiaryWrite;
