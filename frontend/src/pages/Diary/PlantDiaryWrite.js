import React, { useState } from 'react';
import DateDisplay from '../../components/Common/DateDisplay';
import Btn from '../../components/Common/Btn';
import Content from '../../components/Common/Content';
import DiaryProfileHeader from '../../components/Common/DiaryProfileHeader';
import DiaryTodoIcon from '../../components/Diary/DiaryTodoIcon';

import waterIcon from '../../assets/icon/water.png'; 
import fertilizedIcon from '../../assets/icon/fertilized.png'; 
import repottedIcon from '../../assets/icon/repotted.png'; 

const PlantDiaryWrite = () => {
  const [content, setContent] = useState('');
  const [date, setDate] = useState(new Date().toISOString().slice(0, 10)); 

  const handleSave = () => {
    console.log('Diary saved:', { content, date });
  };

  return (
    <div>
      <DateDisplay date={date} />
      <DiaryProfileHeader />
      <h2>오늘 한 일</h2>
      <DiaryTodoIcon src={waterIcon} />
      <DiaryTodoIcon src={fertilizedIcon} />
      <DiaryTodoIcon src={repottedIcon} />
      <Content content={content} setContent={setContent} />
      <Btn content="저장하기" onClick={handleSave} />
    </div>
  );
};

export default PlantDiaryWrite;