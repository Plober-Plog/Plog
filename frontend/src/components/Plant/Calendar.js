import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import { useNavigate } from 'react-router-dom';
import './Calendar.css';

const Calender = ({ plantId }) =>{

  const [value, setValue] = useState(new Date());
  const [checkRecords, setCheckRecords] = useState([]);
  const [diaryRecords, setDiaryRecords] = useState([]);
  const navigate = useNavigate();


  useEffect(() => {
    const year = value.getFullYear();
    const month = value.getMonth();

    // API가 아직 만들어지지 않았기 때문에 dummy json 데이터로 테스트
    const fetchRecords = async () => {
      try {
        const checkResponse = await fetch('/dummy/checkRecords.json');
        const checkData = await checkResponse.json();
        console.log("Check Records:", checkData);
        setCheckRecords(checkData);

        const diaryResponse = await fetch('/dummy/diaryRecords.json');
        const diaryData = await diaryResponse.json();
        console.log("Diary Records:", diaryData);
        setDiaryRecords(diaryData);
      } catch (error) {
        console.error(error);
      }
    };

    fetchRecords();
  }, [value]);

  /* TODO: 일지 작성 or 조회 페이지로 연결하는 부분 체크
    해당 페이지에서 요구하는 방식으로 날짜 데이터가 전달되는지
    console.log 찍어보고 대화하며 체크할 것
  */
  const handleClickDay = (date) => {
    const diaryRecord = diaryRecords.find(diary => new Date(diary.recordDate).toDateString() === date.toDateString());
    if (diaryRecord) {
      // console.log(state);
      console.log(diaryRecord.plantDiaryId);
      console.log(date);
      navigate(`/plant/diary/${diaryRecord}`);
    } else {
      // console.log(state);
      console.log(diaryRecord);
      console.log(date);
      navigate('/plant/diary/write',
        {state: {date: date.toISOString().split('T')[0], plantId}}
      );
      // console.log(state);
    }
  };

  const colorBox = ({ date }) => {
    const checkRecord = checkRecords.find(record => new Date(record.checkDate).toDateString() === date.toDateString());
    const diaryRecord = diaryRecords.find(diary => new Date(diary.recordDate).toDateString() === date.toDateString());
    
    return (
      <div className="color-box">
        <div className={`indicator ${checkRecord && checkRecord.isWatered ? 'watered' : ''}`}></div>
        <div className={`indicator ${checkRecord && checkRecord.isFertilized ? 'fertilized' : ''}`}></div>
        <div className={`indicator ${checkRecord && checkRecord.isRepotted ? 'repotted' : ''}`}></div>
        <div className={`indicator ${diaryRecord ? 'diary' : ''}`}></div>
      </div>
    );
  };

  return (
    <div>
      <Calendar
        onChange={setValue}
        value={value}
        tileContent={colorBox}
        onClickDay={handleClickDay}
      />
    </div>
  );
};

export default Calender;