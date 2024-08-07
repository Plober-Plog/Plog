import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import { useNavigate } from 'react-router-dom';
import './Calendar.css';
import axios from 'axios';

const CustomCalendar = ({ plantId }) => {
  const URI = 'https://i11b308.p.ssafy.io/api';
  const TOKEN = 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoicGxvZy5jb20iLCJleHAiOjE3MjQwNDg3MDYsImlhdCI6MTcyMjgzOTEwNn0.zyGGYRJrG4SELAACBabt-AiBKPOC_TvVsBZdrk8IfZQ'

  const [value, setValue] = useState(new Date());
  const [checkRecords, setCheckRecords] = useState([]);
  const [diaryRecords, setDiaryRecords] = useState([]);
  const navigate = useNavigate();

  const fetchMonthData = async (year, month) => {
    try {
      // console.log(`Fetching data for year: ${year}, month: ${month}`); // 로그 추가
      const checkResponse = await axios.get(`${URI}/user/plant/${plantId}/check`, {
        params: { year, month },
        headers: { 'Authorization': TOKEN }
      });
      
      const diaryResponse = await axios.get(`${URI}/user/plant/${plantId}/diary`, {
        params: { year, month },
        headers: { 'Authorization': TOKEN }
      });
  
      // console.log('checkResponse:', checkResponse.data); // 로그 추가
      // console.log('diaryResponse:', diaryResponse.data); // 로그 추가

      return { checkData: checkResponse.data, diaryData: diaryResponse.data };
    } catch (error) {
      console.error('Error fetching month data:', error.response.data);
      return { checkData: [], diaryData: [] };
    }
  };

  const fetchRecords = async (date) => {
    const year = date.getFullYear();
    const month = date.getMonth(); // JavaScript에서 getMonth()는 0부터 시작하므로 1을 더해줍니다.

    try {
      const currentData = await fetchMonthData(year, month);

      const prevMonth = month === 1 ? 12 : month - 1;
      const prevYear = month === 1 ? year - 1 : year;
      const prevData = await fetchMonthData(prevYear, prevMonth);

      const nextMonth = month === 12 ? 1 : month + 1;
      const nextYear = month === 12 ? year + 1 : year;
      const nextData = await fetchMonthData(nextYear, nextMonth);

      setCheckRecords([...prevData.checkData, ...currentData.checkData, ...nextData.checkData]);
      setDiaryRecords([...prevData.diaryData, ...currentData.diaryData, ...nextData.diaryData]);
    } catch (error) {
      console.error("Calendar Error:", error);
    }
  };

  useEffect(() => {
    fetchRecords(value);
  }, [value, plantId]);

  const formatDate = (date) => {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  };

  const handleClickDay = (date) => {
    const formattedDate = formatDate(date);
    const diaryRecord = diaryRecords.find(diary => new Date(diary.recordDate).toDateString() === date.toDateString());
    const checkRecord = checkRecords.find(check => new Date(check.checkDate).toDateString() === date.toDateString());
    if (diaryRecord || checkRecord) {
      navigate(`/plant/${plantId}/${formattedDate}`, {
        state: {
          date: formattedDate,
          plantId: plantId
        }
        });
    } else {
      navigate(`/plant/${plantId}/${formattedDate}/write`, {
        state: {
          date: formattedDate,
          plantId: plantId
        }
      });
    }
  };

  const colorBox = ({ date }) => {
    const checkRecord = checkRecords.find(record => new Date(record.checkDate).toDateString() === date.toDateString());
    const diaryRecord = diaryRecords.find(diary => new Date(diary.recordDate).toDateString() === date.toDateString());

    return (
      <div className="color-box">
        <div className={`indicator ${checkRecord?.watered ? 'watered' : ''}`}></div>
        <div className={`indicator ${checkRecord?.fertilized ? 'fertilized' : ''}`}></div>
        <div className={`indicator ${checkRecord?.repotted ? 'repotted' : ''}`}></div>
        <div className={`indicator ${diaryRecord ? 'diary' : ''}`}></div>
      </div>
    );
  };

  return (
    <div>
      <Calendar
        onChange={(date) => {
          setValue(date);
          fetchRecords(date);
        }}
        value={value}
        tileContent={colorBox}
        onClickDay={handleClickDay}
        onActiveStartDateChange={({ activeStartDate }) => {
          setValue(activeStartDate);
          fetchRecords(activeStartDate);
        }}
      />
    </div>
  );
};

export default CustomCalendar;
