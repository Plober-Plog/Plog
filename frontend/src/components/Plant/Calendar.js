import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import { useNavigate } from 'react-router-dom';
import './Calendar.css';

const CustomCalendar = ({ plantId }) => {
  const URI = 'https://i11b308.p.ssafy.io/api';
  const [value, setValue] = useState(new Date());
  const [checkRecords, setCheckRecords] = useState([]);
  const [diaryRecords, setDiaryRecords] = useState([]);
  const navigate = useNavigate();

  const fetchMonthData = async (year, month) => {
    const checkResponse = await fetch(`${URI}/user/plant/${plantId}/check?year=${year}&month=${month}`);
    const checkData = await checkResponse.json();
    const diaryResponse = await fetch(`${URI}/user/plant/${plantId}/diary?year=${year}&month=${month}`);
    const diaryData = await diaryResponse.json();
    return { checkData, diaryData };
  };

  // 이전 달 데이터도 가져옵니다 (이전 달 끝부분, 다음 달 첫부분이 캘린더에 함께 보이기 때문에 필요)
  const fetchRecords = async (date) => {
    const year = date.getFullYear();
    const month = date.getMonth() + 1;

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
      navigate(`/plant/${plantId}/${formattedDate}`);
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
        <div className={`indicator ${checkRecord?.isWatered ? 'watered' : ''}`}></div>
        <div className={`indicator ${checkRecord?.isFertilized ? 'fertilized' : ''}`}></div>
        <div className={`indicator ${checkRecord?.isRepotted ? 'repotted' : ''}`}></div>
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
