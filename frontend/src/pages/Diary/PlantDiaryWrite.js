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
  const [content, setContent] = useState('');
  const [date, setDate] = useState(currentDate.toISOString().slice(0, 10)); 
  const [isWatered, setIsWatered] = useState(false);
  const [isFertilized, setIsFertilized] = useState(false);
  const [isRepotted, setIsRepotted] = useState(false);
  const [imgs, setImgs] = useState([]);
  const [plantDiaryId, setplantDiaryId] = useState(null); //현재 날짜에 이미 작성된 일지가 있을 경우 해당 일지의 ID를 저장
  const [isEditMode, setIsEditMode] = useState(false);

  const navigate = useNavigate();

  // 임시 데이터 
  const writerInfoData = {
    profile: defaultImg, 
    nickname: '조이',
    plantTypeId: '몬스테라'
  };
  const weather = 2;
  const humidity = 3;
  const temperature = 30.0;


  // 해당 날짜에 작성된 일지 확인 
  const fetchDiary = async ({recordDate='2024-08-02', plantId=1}) => {
    try {
      const diaryResponse = await fetch(`https://i11b308.p.ssafy.io/api/user/plant/${plantId}/diary?recordDate=${recordDate}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          // 'Authorization': ,
        },
      });
      
      if (!diaryResponse.ok) {
        throw new Error('일지 조회에 실패했습니다.');
      }

      // 처음 페이지 로딩했을 때, 해당 날짜의 일지가 있으면 content 등등 그 내용으로 업로드
      const dataDiary = await diaryResponse.json();
      console.log(dataDiary);
      return dataDiary;

    } catch (error) {
      console.error('Error:', error);
      console.log('새로운 일지를 작성합니다.')
      throw error;
    }
  };

  // 해당 날짜에 작성된 관리 기록 확인
  const fetchPlantCheck = async (checkDate, plantId) => {
    try {
      const plantResponse = await fetch(`https://i11b308.p.ssafy.io/api/user/plant/${plantId}/check?checkDate=${checkDate}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      
      // 서버에서 에러가 났을 때 예외를 발생시키기 위한 코드 catch 로 넘어감
      // if (!response.ok) {
      //   throw new Error('관리 기록 조회에 실패했습니다.');
      // }
      const dataPlant = await plantResponse.json();
      console.log(dataPlant);
      return dataPlant;
    } catch (error) {
      console.error('Error:', error);
      console.log('새로운 일지를 작성합니다.');
      throw error;
    }
  }; 


  // 컴포넌트 로딩 및 날짜 변경 시 일지 조회
  useEffect(() => {
    const getDiaryAndPlantCheck = async () => {
      try {
        const diary = await fetchDiary({recordDate: date, plantId});
        const plantCheck = await fetchPlantCheck({checkDate: date, plantId});
        if (diary || plantCheck) {
          alert('이미 작성된 일기가 있습니다.');
          if (diary) {
            setplantDiaryId(diary.plantDiaryId);
            setContent(diary.content);
            setImgs(diary.image.map(img => ({ url: img.url, id: img.imageId })));
          }
          if (plantCheck) {
            setIsWatered(plantCheck.isWatered);
            setIsFertilized(plantCheck.isFertilized);
            setIsRepotted(plantCheck.isRepotted);
          }
        } 
      } catch (error) {
        // 날짜 변경해도 작성된 일지 없으므로 계속 일지 작성 진행 
        console.error('일지 데이터를 불러오는 중 오류 발생:', error);
        console.log(error);
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
      const diaryWriteResponse = await fetch(
        plantDiaryId ? `https://i11b308.p.ssafy.io/api/user/diary/${plantDiaryId}` : 'https://i11b308.p.ssafy.io/api/user/diary',
        {
          method: plantDiaryId ? 'PATCH' : 'POST',
          headers: {
            'Content-Type': 'application/json',

          },
          body: JSON.stringify(diaryData),
        }
      );

      if (!diaryWriteResponse.ok) throw new Error('일지 저장에 실패했습니다.');

      const plantCheckResponse = await fetch(`https://i11b308.p.ssafy.io/api/user/plant/${plantId}/check`, {
        method: plantDiaryId ? 'PATCH' : 'POST',
        headers: {
          'Content-Type': 'application/json',

        },
        body: JSON.stringify(plantData),
      });

      if (!plantCheckResponse.ok) {
        throw new Error('식물 정보 저장에 실패했습니다.')
      };

      // state 는 임시로 한거고 나중에는 plantDiaryId로 확인할거임
      navigate(`/diary/${date}`, {
        state: {
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
  //   // 일지 작성 요청
  //   try {
  //     const diaryWriteResponse = await fetch(
  //       plantDiaryId ? `/api/user/diary/${plantDiaryId}` : '/api/user/diary', 
  //       {
  //         method: plantDiaryId ? 'PATCH' : 'POST',
  //         headers: {
  //           'Content-Type': 'application/json',
  //         },
  //         body: JSON.stringify(diaryData),
  //       }
  //     );

  //     if (!diaryWriteResponse.ok) {
  //       throw new Error('일지 저장에 실패했습니다.');
  //     }
  //     if (diaryWriteResponse.ok) {
  //       console.log('일지 저장 성공');
  //     }
  //     // navigate(`/diary/${date}`, { state: { diaryData } });
  //   } catch (error) {
  //     console.error('Error:', error);
  //     // alert(error.message);
  //   }
    
  //   // 식물 정보 저장 요청
  //   try {
  //     const method = plantDiaryId? 'PATCH' : 'POST';
  //     const response = await fetch(`/api/user/plant/${plantId}/check`, {
  //       method,
  //       headers: {
  //         'Content-Type': 'application/json',
  //       },
  //       body: JSON.stringify(plantData),
  //     });

  //     if (!response.ok) {
  //       throw new Error('식물 정보 저장에 실패했습니다.');
  //     }

  //     if (response.ok) {
  //       console.log('식물 저장 성공');
  //     }

  //   } catch (error) {
  //     console.error('Error:', error);
  //     alert(error.message);
  //   }

  //   // TODO state 는 임시로 한거고 나중에는 plantDiaryId로 확인할거임
  //   navigate(`/diary/${date}`, 
  //     { state: { 
  //     date, 
  //     content,
  //     weather, 
  //     temperature, 
  //     humidity, 
  //     isWatered, 
  //     isFertilized, 
  //     isRepotted, 
  //     imgs
  //    } });
     
  // };

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
        <Btn content="작성하기" onClick={handleSave} />
      </div>
    </div>
  );
};

export default PlantDiaryWrite;
