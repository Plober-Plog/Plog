import React, { useState, useEffect } from 'react';
import API from '../../apis/api';
import { useLocation, useNavigate } from 'react-router-dom';
import DateDisplay from '../../components/Common/DateDisplay';
import Btn from '../../components/Common/Btn';
import SelectField from '../../components/Common/SelectField';
import TextareaField from '../../components/Common/TextareaField';
import WriterInfo from '../../components/Common/WriterInfo';
import DiaryTodoIcon from '../../components/Diary/DiaryTodoIcon';
import ImgUpload from '../../components/Common/ImgUpload';
import InputField from '../../components/Common/InputField';

import weatherIcon from '../../assets/icon/weather.png'; 
import humidityIcon from '../../assets/icon/humidity.png'; 
import temperatureIcon from '../../assets/icon/temperature.png'; 
import cameraIcon from '../../assets/icon/camera.png';
import waterIcon from '../../assets/icon/water.png'; 
import fertilizedIcon from '../../assets/icon/fertilized.png'; 
import repottedIcon from '../../assets/icon/repotted.png'; 

import './PlantDiaryWrite.css';

const PlantDiaryWrite = () => {  
  const location = useLocation();
  const navigate = useNavigate();
  const { plantId, date: selectedDate } = location.state || {};

  const [content, setContent] = useState('');
  const [date, setDate] = useState(selectedDate); 
  const [isWatered, setIsWatered] = useState(false);
  const [isFertilized, setIsFertilized] = useState(false);
  const [isRepotted, setIsRepotted] = useState(false);
  const [weather, setIsWeather] = useState(0);
  const [humidity, setIsHumidity] = useState(0);
  const [temperature, setIsTemperature] = useState(0);
  const [imgs, setImgs] = useState([]);
  const [plantDiaryId, setPlantDiaryId] = useState(null); //현재 날짜에 이미 작성된 일지가 있을 경우 해당 일지의 ID를 저장
  const [isEditMode, setIsEditMode] = useState(false); // 전체 수정 모드 여부
  const [isEditPlantCheck, setIsEditPlantCheck] = useState(false); // 관리 기록이 있는지 여부를 저장하는 변수
  const [isEditPlantDiary, setIsEditPlantDiary] = useState(false); // 일지 작성 수정 여부
  const [writerInfoData, setWriterInfoData] = useState({});

  // 식물 프로필을 위한 정보 기록 확인  
  const fetchWriterInfo = async (plantId) => {
    try {
      const response = await API.get(`/user/plant/${plantId}/info`);
      setWriterInfoData(response.data);
    } catch (error) {
      console.error('식물 정보 조회 에러:', error);
    }
  };

  // 해당 날짜에 작성된 식물 일지 기록 및 관리 기록 확인 
  const fetchDiaryAndCheck = async ( plantId, date ) => {
    console.log(plantId.data);
    console.log(date.data);
    try {
      const response = await API.get(`/user/plant/${plantId}`, {
        params: { date }
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
      await fetchWriterInfo(plantId);
      const data = await fetchDiaryAndCheck(plantId, date);
      console.log(data);
      if (data.plantDiary || data.plantCheck) {
        setIsEditMode(true);
        if (data.plantDiary) {
          const { plantDiary } = data;
          setPlantDiaryId(plantDiary.plantDiaryId);
          setContent(plantDiary.content);
          setImgs(plantDiary.images.map(img => ({ 
            url: img.url, 
            id: img.nimageId,
            isThumbnail: img.isThumbnail, 
           })));
          setIsWeather(weatherOptions.find(option => option.label === plantDiary.weather).value);
          setIsHumidity(humidityOptions.find(option => option.label === plantDiary.humidity).value);
          setIsTemperature(plantDiary.temperature);
          setIsWatered(false);
          setIsFertilized(false);
          setIsRepotted(false);
          setIsEditMode(true);
          setIsEditPlantDiary(true);
        } 
        if (data.plantCheck) {
          const { plantDiary } = data;
          const { plantCheck } = data;
          setPlantDiaryId(null);
          setContent('');
          setImgs([]);
          setIsWeather(weatherOptions.find(option => option.label === plantDiary.weather).value);
          setIsHumidity(humidityOptions.find(option => option.label === plantDiary.humidity).value);
          setIsTemperature(plantDiary.temperature);
          setIsWatered(plantCheck.isWatered);
          setIsFertilized(plantCheck.isFertilized);
          setIsRepotted(plantCheck.isRepotted);  
          setIsEditMode(true); 
          setIsEditPlantCheck(true);    
        } 

        if (data.plantDiary && data.plantCheck) {
          const { plantDiary } = data;
          const { plantCheck } = data;
          setPlantDiaryId(plantDiary.plantDiaryId);
          setContent(plantDiary.content);
          setImgs(plantDiary.images.map(img => ({ 
            url: img.url, 
            id: img.imageId, 
           })));
          setIsWeather(weatherOptions.find(option => option.label === plantDiary.weather).value);
          setIsHumidity(humidityOptions.find(option => option.label === plantDiary.humidity).value);
          setIsTemperature(plantDiary.temperature);
          setIsWatered(plantCheck.isWatered);
          setIsFertilized(plantCheck.isFertilized);
          setIsRepotted(plantCheck.isRepotted);   
          setIsEditMode(true); 
          setIsEditPlantCheck(true);
          setIsEditPlantDiary(true);
        }
      } else {
        setPlantDiaryId(null);
        setContent('');
        setImgs([]);
        setIsWatered(false);
        setIsFertilized(false);
        setIsRepotted(false); 
        setIsEditMode(false);
        console.log('새로운 일지를 작성');
      }
    } catch (error) {
      console.error('일지 데이터를 불러오는 중 오류 발생:', error);
    }
  };
  getDiaryAndPlantCheck();
}, [date, plantId]);

useEffect(() => {
  console.log("Updated plantDiaryId:", plantDiaryId);
}, [plantDiaryId]);

  // 이미지 업로드 .. => 잘 모루겟어서 지피티한테 물어봄 ㅜㅜ .. 이미지 부분 수정 가능성 높습니다.. ..
  
  const handleImageUpload = (event) => {
    console.log(event.target.files);
    setImgs(Array.from(event.target.files)); // 파일 입력에서 파일 배열을 만들기
  };

  const handleDeleteImage = (index) => {
    const newImgs = imgs.filter((_, i) => i !== index);
    setImgs(newImgs);
    if (index === 0 && newImgs.length > 0) {
      newImgs[0].isThumbnail = true;
    }
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
  
  // 저장 버튼 클릭
  const handleSave = async () => {
    const formattedDate = date instanceof Date ? date.toISOString().split('T')[0] : new Date(date).toISOString().split('T')[0];
    const thumbnailIdx = 0;
    
    const diaryData = new FormData();
    diaryData.append('plantDiaryId', plantDiaryId);
    diaryData.append('plantId', plantId);
    diaryData.append('weather', 1);
    diaryData.append('temperature', 1);
    diaryData.append('humidity', 1);
    diaryData.append('content', content);
    console.log(imgs);
    diaryData.append('thumbnailIdx', thumbnailIdx);
    diaryData.append('recordDate', formattedDate);
    
    
    imgs.forEach((img) => {
      diaryData.append('images', img);  // 'images' key를 사용하여 각각의 파일을 추가
    });
    
    console.log(diaryData);
    
    const diaryDataPatch = {
      plantDiaryId,
      plantId,
      weather,
      temperature,
      humidity,
      content,
      recordDate : formattedDate,
      thumbnailIdx,
    }
    
    const plantData = {
      plantId,
      isWatered,
      isFertilized,
      isRepotted,
      checkDate: formattedDate,
    }
    console.log(plantData);
    
    
    // 일지작성요청 1
    try {
      if (isEditMode) {
        const diaryWriteResponse = await API.patch(`user/diary/${plantDiaryId}`, diaryDataPatch, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
        if (diaryWriteResponse.status !== 200) {
          throw new Error('일지 수정에 실패했습니다.');
        }

        const plantCheckResponse = await API.patch(`user/plant/${plantId}/check`,plantData, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
        if (plantCheckResponse.status !== 200) {
          throw new Error('식물 정보 수정에 실패했습니다.');
        }
      }

      console.log(plantDiaryId);
      console.log(plantData);  
      console.log(diaryDataPatch);
      
      if (!isEditMode) {
        const diaryWriteResponse = await API.post(`user/diary`, diaryData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });
        if (diaryWriteResponse.status !== 200) {
          throw new Error('일지 작성에 실패했습니다.');
        }

        const plantCheckResponse = await API.post(`user/plant/${plantId}/check`,plantData, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
        if (plantCheckResponse.status !== 200) {
          throw new Error('식물 정보 저장에 실패했습니다.');
        }
      } 
      
      
      navigate(`/plant/${plantId}/${formattedDate}`, {
        state: {
          plantId,
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
    setDate(newDate)
    if (data.plantDiary || data.plantCheck) {
      if (window.confirm('이미 작성된 일지가 있습니다. 확인하시겠습니까?')) {
        navigate(`/plant/${plantId}/${newDate}`, {
          state: {
            date: newDate,
            plantId: plantId
          }
        });
      }    
    }
  };
  
  const weatherOptions = [
    { value: 1, label: 'SUNNY' },
    { value: 2, label: 'CLOUDY' },
    { value: 3, label: 'VERY_CLOUDY' },
    { value: 4, label: 'RAINY' },
    { value: 5, label: 'SNOWY' },
  ];

  const humidityOptions = [
    { value: 1, label: 'DRY' },
    { value: 2, label: 'CLEAN' },
    { value: 3, label: 'NORMAL' },
    { value: 4, label: 'MOIST' },
    { value: 5, label: 'WET' },
  ];

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
        />
      </div>
      <div className="section">
        <div className="todo-icons">
          <DiaryTodoIcon src={weatherIcon} />
          <DiaryTodoIcon src={humidityIcon} />
          <DiaryTodoIcon src={temperatureIcon} />
        </div>
        <SelectField
            value={weather}
            onChange={(e) => setIsWeather(Number(e.target.value))}
            options={weatherOptions}
            className="drop-box"
          />
        <SelectField
            value={humidity}
            onChange={(e) => setIsHumidity(Number(e.target.value))}
            options={humidityOptions}
            className="drop-box"
          />
        <InputField 
          type="number"
          value={temperature}
          onChange={(e) => setIsTemperature(Number(e.target.value))}
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
        <TextareaField 
          placeholder='일지를 입력하세요.'
          value={content} 
          onChange={(e) => setContent(e.target.value)}   
        />
      </div>
      <div>
        <Btn content={isEditMode ? "수정하기" : "작성하기"} onClick={handleSave} />
      </div>
    </div>
  );
};

export default PlantDiaryWrite;
