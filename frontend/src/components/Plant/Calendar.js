import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import { useNavigate } from 'react-router-dom';
import './Calendar.css';

const Calender = ({ plantId }) =>{

  const URI = 'https://i11b308.p.ssafy.io/api'
  const [value, setValue] = useState(new Date());
  const [checkRecords, setCheckRecords] = useState([]);
  const [diaryRecords, setDiaryRecords] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchRecords = async () => {
      const year = value.getFullYear();
      const month = value.getMonth() +1;

      try {
        const checkResponse = await fetch(`${URI}/user/plant/${plantId}/check?year=${year}&month=${month}`)
        const checkData = await checkResponse.json();
        setCheckRecords(checkData);

        const diaryResponse = await fetch(`${URI}/user/plant/${plantId}/diary?year=${year}&month=${month}`)
        const diaryData = await diaryResponse.json();
        setDiaryRecords(diaryData);
      } catch (error) {
        console.error("fetch 함수 ERROR(아마도):", error);
      }
    };

    fetchRecords();
  }, [value, plantId]);

  const handleClickDay = (date) => {
    const diaryRecord = diaryRecords.find(diary => new Date(diary.recordDate).toDateString() === date.toDateString());
    if (diaryRecord) {
      navigate(`/plant/diary/${diaryRecord.plantDiaryId}`);
    } else {
      navigate(`/plant/diary/write`, {
        state: {
          date: date.toISOString().split('T')[0],
          plantId
        }
      });
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