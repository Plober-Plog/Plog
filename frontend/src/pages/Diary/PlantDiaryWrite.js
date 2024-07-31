import React, { useState, useEffect } from 'react';
import DateDisplay from '../../components/Common/DateDisplay';
import Btn from '../../components/Common/Btn';
import Content from '../../components/Common/Content';
import WriterInfo from '../../components/Common/WriterInfo';
import DiaryTodoIcon from '../../components/Diary/DiaryTodoIcon';

import waterIcon from '../../assets/icon/water.png'; 
import fertilizedIcon from '../../assets/icon/fertilized.png'; 
import repottedIcon from '../../assets/icon/repotted.png'; 

const PlantDiaryWrite = ({ existingDiaries = [] }) => {
  const [diaries, setDiaries] = useState(existingDiaries);
  const [content, setContent] = useState('');
  const [date, setDate] = useState(new Date().toISOString().slice(0, 10)); 
  const [isWatered, setIsWatered] = useState(false);
  const [isFertilized, setIsFertilized] = useState(false);
  const [isRepotted, setIsRepotted] = useState(false);
  const [isExistingDiary, setIsExistingDiary] = useState(false);
  
  const writerInfoData = {
    profile: 'https://via.placeholder.com/150', 
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

  const toggleWatered = () => {
    setIsWatered(!isWatered);
  };

  const toggleFertilized = () => {
    setIsFertilized(!isFertilized);
  };

  const toggleRepotted = () => {
    setIsRepotted(!isRepotted);
  };

  const handleSave = () => {
    const diaryData = {
      date,
      content,
      isWatered,
      isFertilized,
      isRepotted,
    };

    const updatedDiaries = diaries.filter(diary => diary.date !== date).concat(diaryData);
    setDiaries(updatedDiaries);
    console.log('다이어리 저장:', diaryData);
  };

  return (
    <div>
      <DateDisplay date={date} setDate={setDate} />
      <WriterInfo data={writerInfoData} type="plant"/>
      {/* 사진 첨부하기 컴포넌트 추가해야함 */}
      <div>
        <h2>오늘 한 일</h2>
        <DiaryTodoIcon src={waterIcon} active={isWatered} onClick={toggleWatered} />
        <DiaryTodoIcon src={fertilizedIcon} active={isFertilized} onClick={toggleFertilized} />
        <DiaryTodoIcon src={repottedIcon} active={isRepotted} onClick={toggleRepotted} />
      </div>
      <div>
        <h2>일지 작성</h2>
        <Content content={content} setContent={setContent} />
      </div>
      <Btn content={isExistingDiary ? "수정하기" : "작성하기"} onClick={handleSave} />
    </div>
  );
};

export default PlantDiaryWrite;
