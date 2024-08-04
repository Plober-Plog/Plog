import React, { useState, useEffect } from 'react';
import axios from 'axios';
// import API from '../path/to/api';
import { useLocation, useNavigate } from 'react-router-dom';
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

//TODO [차유림] 현재는 location 받아오는 게 없어서 오류가 남 우선 주석처리했음
const PlantDiaryWrite = ({currentDate = new Date(), plantId}) => {  
  // const location = useLocation();
  const navigate = useNavigate();
  // const { plantId, date: selectedDate } = location.state;

  const URI = 'https://i11b308.p.ssafy.io/api'
  const [content, setContent] = useState('');
  // const [date, setDate] = useState(selectedDate); 
  const [date, setDate] = useState(currentDate); 
  const [isWatered, setIsWatered] = useState(false);
  const [isFertilized, setIsFertilized] = useState(false);
  const [isRepotted, setIsRepotted] = useState(false);
  const [imgs, setImgs] = useState([]);
  const [plantDiaryId, setPlantDiaryId] = useState(null); //현재 날짜에 이미 작성된 일지가 있을 경우 해당 일지의 ID를 저장
  const [isEditMode, setIsEditMode] = useState(false);


  // 임시 데이터 
  const writerInfoData = {
    profile: defaultImg, 
    nickname: '조이',
    plantTypeId: '몬스테라'
  };
  const weather = 2;
  const humidity = 3;
  const temperature = 30.0;


  // 해당 날짜에 작성된 식물 일지 기록 확인 
  // TODO [차유림] 그럼 이걸 식물 일지 기록 조회가 아니라 일지 조회에서 해야하나 ? 바뀌면 추가해볼게염.. 
  const fetchDiary = async (plantId, recordDate) => {
    try {
      const response = await axios.get(`${URI}/user/plant/${plantId}/diary`, {
        params: { recordDate },
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoicGxvZy5jb20iLCJleHAiOjE3MjM5MTIyNjEsImlhdCI6MTcyMjcwMjY2MX0.wp3fqP8MHxSy4i-CUZUHnt85iRjS0cksuhu4bbtvhzw`,
        },
      });
      console.log(response);
      return response.data;
    } catch (error) {
      console.error('작성된 식물일지 확인 에러:', error);
      return null;
    }
  };

  // 해당 날짜에 작성된 관리 기록 확인
  const fetchPlantCheck = async (plantId, checkDate) => {
    try {
      const response = await axios.get(`${URI}/user/plant/${plantId}/check`, {
        params: { checkDate },
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoicGxvZy5jb20iLCJleHAiOjE3MjM5MTIyNjEsImlhdCI6MTcyMjcwMjY2MX0.wp3fqP8MHxSy4i-CUZUHnt85iRjS0cksuhu4bbtvhzw`,
        },
      });
      console.log(response);
      return response.data;
    } catch (error) {
      console.error('관리 기록 확인 에러:', error);
      return null;
    }
  };


  // 컴포넌트 로딩 및 날짜 변경 시 일지 조회
  useEffect(() => {
    const getDiaryAndPlantCheck = async () => {
      try {
        const diary = await fetchDiary(plantId, date);
        const plantCheck = await fetchPlantCheck(plantId, date);
        if (diary || plantCheck) {
          setIsEditMode(true);
          if (diary) {
            setPlantDiaryId(diary.plantDiaryId);
            setContent(diary.content);
            setImgs(diary.image.map(img => ({ url: img.url, id: img.imageId })));
          }
          if (plantCheck) {
            setIsWatered(plantCheck.isWatered);
            setIsFertilized(plantCheck.isFertilized);
            setIsRepotted(plantCheck.isRepotted);
          }
        } else {
          setIsEditMode(false);
          console.log('새로운 일지를 작성');
        }
      } catch (error) {
        console.error('일지 데이터를 불러오는 중 오류 발생:', error);
      }
    };
    getDiaryAndPlantCheck();
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
        url: img.url,
        isThumbnail: index === 0 // 첫 번째 이미지를 대표 사진으로 설정
      })),
      recordDate: date,
    };

    const plantData = {
      isWatered,
      isFertilized,
      isRepotted,
      checkDate: date,
    }
    // 일지작성요청 2
    try {
      const diaryWriteResponse = await axios({
        method: plantDiaryId ? 'PATCH' : 'POST',
        url: plantDiaryId ? `${URI}/user/diary/${plantDiaryId}` : `${URI}/user/diary`,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoicGxvZy5jb20iLCJleHAiOjE3MjM5MTIyNjEsImlhdCI6MTcyMjcwMjY2MX0.wp3fqP8MHxSy4i-CUZUHnt85iRjS0cksuhu4bbtvhzw`,
        },
        data: diaryData,
      });

      if (diaryWriteResponse.status !== 200) {
        throw new Error('일지 저장에 실패했습니다.');
      }

      const plantCheckResponse = await axios({
        method: 'POST',
        url: `${URI}/user/plant/${plantId}/check`,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoicGxvZy5jb20iLCJleHAiOjE3MjM5MTIyNjEsImlhdCI6MTcyMjcwMjY2MX0.wp3fqP8MHxSy4i-CUZUHnt85iRjS0cksuhu4bbtvhzw`,
        },
        data: plantData,
      });

      if (plantCheckResponse.status !== 200) {
        throw new Error('식물 정보 저장에 실패했습니다.');
      }

      navigate(`/plant/${plantId}/${date}`, {
        state: {
          plantDiaryId,
          date,
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
    setDate(newDate);
  };


  return (
    <div className="plant-diary-container">
      <div className="section">
        <DateDisplay date={date} setDate={handleDateChange} />
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
        <Btn content={isEditMode ? "수정하기" : "작성하기"} onClick={handleSave} />
      </div>
    </div>
  );
};

export default PlantDiaryWrite;
