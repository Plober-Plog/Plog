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
import waterIcon from '../../assets/icon/water-default.png'; 
import fertilizedIcon from '../../assets/icon/fertilized-default.png'; 
import repottedIcon from '../../assets/icon/repotted-default.png'; 
import waterFillIcon from '../../assets/icon/water-select.png'; 
import fertilizedFillIcon from '../../assets/icon/fertilized-select.png'; 
import repottedFillIcon from '../../assets/icon/repotted-select.png';

import './PlantDiaryWrite.css';

const PlantDiaryWrite = () => {  
  const location = useLocation();
  const navigate = useNavigate();
  const { plantId, date: selectedDate, diaryData } = location.state || {};

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

  // 옵션 값 매핑 함수 추가
  const mapWeatherStringToValue = (label) => {
    const option = weatherOptions.find(option => option.label === label);
    return option ? option.value : 0;
  };

  const mapHumidityStringToValue = (label) => {
    const option = humidityOptions.find(option => option.label === label);
    return option ? option.value : 0;
  };

  // 날씨 정보 가져오기 
  const fetchWeatherInfo = async () => {
    try {
      const response = await API.get('/user/diary/get-weather', {
        params: { date }
      });
      setIsWeather(response.data.weather);  
      setIsHumidity(response.data.humidity);  
      setIsTemperature(response.data.temperature);  
    } catch (error) {
      console.error('날씨 정보 조회 에러:', error);
    }
  };

  // 해당 날짜에 작성된 식물 일지 기록 및 관리 기록 확인 
  const fetchDiaryAndCheck = async ( plantId, date ) => {
    try {
      const response = await API.get(`/user/plant/${plantId}`, {
        params: { date }
      });
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
      await fetchWeatherInfo();
      const data = await fetchDiaryAndCheck(plantId, date);
      if (data.plantDiary || data.plantCheck) {
        const { plantDiary } = data;
        const { plantCheck } = data;
        setIsEditMode(true);
        setPlantDiaryId(plantDiary.plantDiaryId);
        setContent(plantDiary.content);
        setImgs(plantDiary.images.map(img => ({ 
          url: img.url, 
          id: img.imageId, 
        })));
        setIsWeather(mapWeatherStringToValue(plantDiary.weather));  
        setIsHumidity(mapHumidityStringToValue(plantDiary.humidity));
        setIsTemperature(plantDiary.temperature);
        setIsWatered(plantCheck.isWatered);
        setIsFertilized(plantCheck.isFertilized);
        setIsRepotted(plantCheck.isRepotted);   
        setIsEditMode(true); 
      } else {
        setPlantDiaryId(null);
        setContent('');
        setImgs([]);
        setIsWatered(false);
        setIsFertilized(false);
        setIsRepotted(false); 
        setIsEditMode(false);
      }
    } catch (error) {
      console.error('일지 데이터를 불러오는 중 오류 발생:', error);
    }
  };
  getDiaryAndPlantCheck();
}, [date, plantId]);

  // 이미지 업로드
  const handleImageUpload = (event) => {
    const files = Array.from(event.target.files);
    if (imgs.length + files.length > 5) {
      alert('이미지는 최대 5개까지 업로드할 수 있습니다.');
      return;
    }
    const newImgs = files.map(file => ({ 
      url: URL.createObjectURL(file), 
      file 
    }));
    setImgs((prevImgs) => [...prevImgs, ...newImgs]);
  };
  
  // 이미지 삭제 
  const handleDeleteImage = (index) => {
    const newImgs = imgs.filter((_, i) => i !== index);
    setImgs(newImgs);
  };

  const toggleWatered = () => {
    const newState = !isWatered;
    setIsWatered(newState);
  };

  const toggleFertilized = () => {
    const newState = !isFertilized;
    setIsFertilized(newState);
  };

  const toggleRepotted = () => {
    const newState = !isRepotted;
    setIsRepotted(newState);
  };
  
  // 저장 버튼 클릭
  const handleSave = async () => {
    const formattedDate = date instanceof Date ? date.toISOString().split('T')[0] : new Date(date).toISOString().split('T')[0];
    const thumbnailIdx = 0;
    
    const diaryData = new FormData();
    diaryData.append('plantDiaryId', plantDiaryId);
    diaryData.append('plantId', plantId);
    diaryData.append('weather', weather);
    diaryData.append('temperature',temperature);
    diaryData.append('humidity', humidity);
    diaryData.append('content', content);
    diaryData.append('thumbnailIdx', thumbnailIdx);
    diaryData.append('recordDate', formattedDate);
    
    
    imgs.forEach((img) => {
      diaryData.append('images', img.file);  // 'images' key를 사용하여 각각의 파일을 추가
    });
        
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
    <div className="plant-diary-write-container">
      <div className="mb-5 cardlist-subtitle">
        <DateDisplay date={date} setDate={handleDateChange} />
      </div>
      <div className="mb-4 plant-diary-write-section">
        <WriterInfo data={writerInfoData} type="plant" />
      </div>
      <div className="mb-4 plant-diary-write-section plant-diary-write-camera-container">
        {!isEditMode ? (
          <div>
            <ImgUpload 
              cameraIcon={cameraIcon}
              imgs={imgs} 
              handleImageUpload={handleImageUpload} 
              handleDeleteImage={handleDeleteImage} 
            />
          </div>
        ) : (
          <p>사진은 수정할 수 없습니다.</p>
        )}
      </div>
      <div className="mb-4 plant-diary-write-section plant-diary-write-icons">
        <h2 className='mb-4 cardlist-subtitle'>날씨 정보</h2> {/* 제목 추가 */}
        <div className="mb-2 plant-diary-write-icon-container">
          <div className="plant-diary-write-icon">
            <DiaryTodoIcon 
              src={weatherIcon} 
              active={false} 
              onClick={() => {}} 
            />
            <SelectField
              value={weather}
              onChange={(e) => setIsWeather(Number(e.target.value))}
              options={weatherOptions}
            />
          </div>
          <div className="plant-diary-write-icon">
            <DiaryTodoIcon 
              src={humidityIcon} 
              active={false}
              onClick={() => {}}
            />
            <SelectField
              value={humidity}
              onChange={(e) => setIsHumidity(Number(e.target.value))}
              options={humidityOptions}
            />
          </div>
          <div className="plant-diary-write-icon">
            <DiaryTodoIcon 
              src={temperatureIcon} 
              active={false} 
              onClick={() => {}} 
            />
            <InputField 
              type="number"
              value={temperature.toFixed(1)}  
              onChange={(e) => setIsTemperature(Number(parseFloat(e.target.value).toFixed(1)))}  
            />
          </div>
        </div>
      </div>
      <div className="plant-diary-write-section plant-diary-write-icons">
        <h2 className='mb-4 cardlist-subtitle'>오늘 한 일</h2>
        <div className="mb-4 plant-diary-write-icon-container">
          <div className="plant-diary-write-icon">
            <DiaryTodoIcon 
              src={waterIcon} 
              fillSrc={waterFillIcon} 
              active={isWatered} 
              onClick={toggleWatered} 
            />
          </div>
          <div className="plant-diary-write-icon">
            <DiaryTodoIcon 
              src={fertilizedIcon} 
              fillSrc={fertilizedFillIcon} 
              active={isFertilized} 
              onClick={toggleFertilized} 
            />
          </div>
          <div className="plant-diary-write-icon">
            <DiaryTodoIcon 
              src={repottedIcon} 
              fillSrc={repottedFillIcon} 
              active={isRepotted} 
              onClick={toggleRepotted} 
            />
          </div>
        </div>
      </div>
      <div className="plant-diary-write-section">
        <h2 className='mb-4 cardlist-subtitle'>일지 작성</h2>
        <div>
        <TextareaField 
          placeholder='일지를 입력하세요.'
          value={content} 
          className="plant-diary-write-textarea"  
          onChange={(e) => setContent(e.target.value)}   
        />
        </div>
      </div>
      <div className='plant-diary-write-btn'>
        <Btn content={isEditMode ? "수정하기" : "작성하기"} onClick={handleSave} className="plant-diary-write-button" />
      </div>
    </div>
  );
};

export default PlantDiaryWrite;
