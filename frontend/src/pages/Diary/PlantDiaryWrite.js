import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import DateDisplay from '../../components/Common/DateDisplay';
import Btn from '../../components/Common/Btn';
import Content from '../../components/Common/Content';
import WriterInfo from '../../components/Common/WriterInfo';
import DiaryTodoIcon from '../../components/Diary/DiaryTodoIcon';
import ImgUpload from '../../components/Common/ImgUpload';

import cameraIcon from '../../assets/icon/camera.png';
import waterIcon from '../../assets/icon/water.png'; 
import fertilizedIcon from '../../assets/icon/fertilized.png'; 
import repottedIcon from '../../assets/icon/repotted.png'; 
import './PlantDiaryWrite.css';

const PlantDiaryWrite = ({ existingDiaries = [] }) => {
  const [diaries, setDiaries] = useState(existingDiaries);
  const [content, setContent] = useState('');
  const [date, setDate] = useState(new Date().toISOString().slice(0, 10)); 
  const [isWatered, setIsWatered] = useState(false);
  const [isFertilized, setIsFertilized] = useState(false);
  const [isRepotted, setIsRepotted] = useState(false);
  const [isExistingDiary, setIsExistingDiary] = useState(false);
  const [imgs, setImgs] = useState([]);

  const navigate = useNavigate();

  // 임시 데이터 
  const writerInfoData = {
    profile: 'https://via.placeholder.com/50', 
    nickname: '조이',
    plantTypeId: '몬스테라'
  };

  useEffect(() => {
    const diary = diaries.find(d => d.date === date);
    if (diary) {
      setContent(diary.content);
      setIsWatered(diary.isWatered);
      setIsFertilized(diary.isFertilized);
      setIsRepotted(diary.isRepotted);
      setIsExistingDiary(true);
    } else {
      setContent('');
      setIsWatered(false);
      setIsFertilized(false);
      setIsRepotted(false);
      setIsExistingDiary(false);
    }
  }, [date, diaries]);

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

  const handleSave = () => {
    const diaryData = {
      date,
      content,
      isWatered,
      isFertilized,
      isRepotted,
      imgs,
    };

    const updatedDiaries = diaries.filter(diary => diary.date !== date).concat(diaryData);
    setDiaries(updatedDiaries);
    console.log('다이어리 저장:', diaryData);

    navigate(`/diary/${date}`, { state: { diaryData } });
  };

  return (
    <div className="plant-diary-container">
      <div className="section">
        <DateDisplay date={date} setDate={setDate} />
      </div>
      <div className="section">
        <WriterInfo data={writerInfoData} type="plant"/>
      </div>
      {/* 사진 첨부하기 컴포넌트 추가해야함 */}
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
