import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import DateDisplay from '../../components/Common/DateDisplay';
import Btn from '../../components/Common/Btn';
import Content from '../../components/Common/Content';
import WriterInfo from '../../components/Common/WriterInfo';
import DiaryTodoIcon from '../../components/Diary/DiaryTodoIcon';
import ImgUpload from '../../components/Common/ImgUpload';

import defaultImg from '../../assets/icon/default.png';
import cameraIcon from '../../assets/icon/camera.png';
import waterIcon from '../../assets/icon/water.png'; 
import fertilizedIcon from '../../assets/icon/fertilized.png'; 
import repottedIcon from '../../assets/icon/repotted.png'; 
import './PlantDiaryWrite.css';

const PlantDiaryWrite = ({ currentDate = new Date(), plantId }) => { 
  //임시데이터
  // const currentDate = new Date();  
  // const plantId = '1'; 
  const [diaries, setDiaries] = useState([]);
  const [content, setContent] = useState('');
  const [date, setDate] = useState(currentDate.toISOString().slice(0, 10)); 
  const [isWatered, setIsWatered] = useState(false);
  const [isFertilized, setIsFertilized] = useState(false);
  const [isRepotted, setIsRepotted] = useState(false);
  const [isExistingDiary, setIsExistingDiary] = useState(false);
  const [imgs, setImgs] = useState([]);

  const navigate = useNavigate();

  // 임시 데이터 
  const writerInfoData = {
    profile: defaultImg, 
    nickname: '조이',
    plantTypeId: '몬스테라'
  };
  const weather = '강수량 많음'
  const humidity = '습함'
  const temperature = '기온 높음'

  // 해당 날짜에 작성된 일지 확인 -> 날짜로 찾기 필요할듯..
  const fetchDiary = async ({currentDate, plantId}) => {
    try {
      const response = await fetch(`/api/user/diary/${plantId}/${currentDate}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
  
      if (!response.ok) {
        throw new Error('일지 조회에 실패했습니다.');
      }
  
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Error:', error);
      throw error;
    }
  };

  // 이 부분 왜 useEffect 가 2개인지? 
  // 날짜 변경하면 일지 작성 여부 확인
  useEffect(() => {
    const diary = fetchDiary({date, plantId});
    if (diary) {
      alert('이미 작성된 일기가 있습니다.');
      console.log(diary)
    }
  }, [date]);


  // 컴포넌트 로딩 및 날짜 변경 시 일지 조회
  useEffect(() => {
    const getDiary = async () => {
      try {
        const diary = await fetchDiary(date, plantId);
        if (diary) {
          alert('이미 작성된 일기가 있습니다.');
        } 
      } catch (error) {
        console.error('일지 데이터를 불러오는 중 오류 발생:', error);
      }
    };
    getDiary();
  }, [date, plantId]);


  // 정보 입력
  const handleImageUpload = (event) => {
    const files = Array.from(event.target.files);
    if (files.length + imgs.length > 5) {
      alert('최대 5장까지 업로드할 수 있습니다.');
      return;
    }
    const newImgs = files.map(file => URL.createObjectURL(file));
    setImgs(prevImgs => [...prevImgs, ...newImgs]);
  };

  const handleDeleteImage = (index) => {
    setImgs(prevImgs => prevImgs.filter((_, i) => i !== index));
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
    const diaryData = {
      plantId,
      weather,
      temperature,
      humidity,
      content,
      image: imgs.map((img, index) => ({
        url: img,
        isThumbnail: index === 0 // 첫 번째 이미지를 대표 사진으로 설정
      })),
      recordDate: date,
    };

    const plantData = {
      isWatered,
      isFertilized,
      isRepotted,
      date
    }

    // 일지 작성 요청
    try {
      const response = await fetch('/api/user/diary', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: diaryData,
      });

      if (!response.ok) {
        throw new Error('다이어리 저장에 실패했습니다.');
      }

      navigate(`/diary/${date}`, { state: { diaryData } });
    } catch (error) {
      console.error('Error:', error);
      alert(error.message);
    }
    
    // 식물 정보 저장 요청
    try {
      const response = await fetch(`/api/user/plant/${plantId}/check`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: plantData,
      });

      if (!response.ok) {
        throw new Error('다이어리 저장에 실패했습니다.');
      }

    } catch (error) {
      console.error('Error:', error);
      alert(error.message);
    }

    navigate(`/diary/${date}`, { state: { 
      date, content, weather, temperature, humidity, isWatered, isFertilized, isRepotted, imgs
     } });
     
  };

  return (
    <div className="plant-diary-container">
      <div className="section">
        <DateDisplay date={date} setDate={setDate} />
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
        <Btn content={isExistingDiary ? "수정하기" : "작성하기"} onClick={handleSave} />
      </div>
    </div>
  );
};

export default PlantDiaryWrite;
