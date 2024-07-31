import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import { useNavigate } from 'react-router-dom';

const Calander = ({plantId}) =>{

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


  // 페이지 이동 함수
  // 해당 날짜를 클릭했을 때,
  // diaryRecords 기록이 있다면 -> 해당 날짜와 함께 PlantDiaryDetail 페이지로 이동
  // diaryRecords 기록이 없다면 -> 해당 날짜와 함께 PlantDiaryWrite 페이지로 이동
  // checkReCords 기록과는 무관합니다.
  // 이후 PlantDiaryDetail, PlantDiaryWrite 페이지를 pull 받아본 뒤 test 할 예정입니다.
  // const handleClickDay = (date) => {
  // };


  const colorBox = ({ date }) => {
    const checkRecord = checkRecords.find(record => new Date(record.checkDate).toDateString() === date.toDateString());
    const diaryRecord = diaryRecords.find(diary => new Date(diary.recordDate).toDateString() === date.toDateString());
    
    return (
      <div style={styles.box}>
        <div style={{
          ...styles.indicator,
          ...(checkRecord && checkRecord.isWatered ? styles.watered : {})
        }}>
        </div>
        <div style={{
          ...styles.indicator,
          ...(checkRecord && checkRecord.isFertilized ? styles.fertilized : {})
        }}>
        </div>
        <div style={{
          ...styles.indicator,
          ...(checkRecord && checkRecord.isRepotted ? styles.repotted : {})
        }}>
        </div>
        <div style={{
          ...styles.indicator,
          ...(diaryRecord ? styles.diary : {})
        }}>
        </div>
      </div>
    );
  };


  // 임시 styles
  const styles = {
    box: {
      position: 'relative',
      height: '100%',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
    },
    indicator: {
      width: '80%',
      height: '6px',
      backgroundColor: 'white',
    },
    watered: {
      backgroundColor: '#A8D8E7',
    },
    fertilized: {
      backgroundColor: '#FFB9B9',
    },
    repotted: {
      backgroundColor: '#99DA9B',
    },
    diary: {
      backgroundColor: '#F7D98B',
    }
  };


  return (
    <div>
      <Calendar
        onChange={setValue}
        value={value}
        tileContent={colorBox}
      />
    </div>
  );
};

export default Calander;