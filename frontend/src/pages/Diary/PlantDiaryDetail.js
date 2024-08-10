import React, { useState, useEffect, useRef } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import API from '../../apis/api';
import DiaryTodoIcon from '../../components/Diary/DiaryTodoIcon';
import ImageSlider from '../../components/Common/ImgSlider';
import DiaryWeather from '../../components/Diary/DiaryWeather';
import DiaryDetailContent from '../../components/Diary/DiaryDetailContent';
import Btn from '../../components/Common/Btn';
import html2canvas from 'html2canvas';

import pencilIcon from '../../assets/icon/pencil.png'; 
import waterIcon from '../../assets/icon/water.png'; 
import fertilizedIcon from '../../assets/icon/fertilized.png'; 
import repottedIcon from '../../assets/icon/repotted.png'; 
import weatherIcon from '../../assets/icon/weather.png'; 
import humidityIcon from '../../assets/icon/humidity.png'; 
import temperatureIcon from '../../assets/icon/temperature.png'; 
import './PlantDiaryWrite.css';

const PlantDiaryDetail = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const reportRef = useRef(null);

  const { plantId, date } = location.state;
  const [plantCheck, setPlantCheck] = useState(null);
  const [plantDiary, setPlantDiary] = useState(null);

  // console.log(location);

  // 일지 디테일 불러오기
  useEffect(() => {
    const getPlantDetailDiary = async () => {
      try {
        console.log(plantId, date);
        const response = await API.get(`/user/plant/${plantId}`, {
          params: { date }
        });

        const data = response.data;
        console.log(data);
        setPlantCheck(data.plantCheck || {});
        setPlantDiary(data.plantDiary || {});

      } catch (error) {
        console.error('작성된 일지 확인 에러:', error);
      }
    };

    getPlantDetailDiary();
  }, [plantId, date]);

  if (!plantCheck || !plantDiary) {
    return <div>Loading...</div>;
  }

  const isWatered = plantCheck.isWatered || false;
  const isFertilized = plantCheck.isFertilized || false;
  const isRepotted = plantCheck.isRepotted || false;

  const weather = plantDiary.weather || 1;
  const temperature = plantDiary.temperature || 0.0;
  const humidity = plantDiary.humidity || 1;
  const content = plantDiary.content || '작성된 일지 내용이 없습니다.';
  const images = plantDiary.images || [];
  const plantDiaryId = plantDiary.plantDiaryId;

  const handleEdit = () => {
    navigate(`/plant/${plantId}/${date}/write`, {
      state: {
        diaryData: {
          plantDiaryId,
          date,
          content,
          weather,
          temperature,
          humidity,
          isWatered,
          isFertilized,
          isRepotted,
          imgs: images.map(img => ({ url: img.url })),
          isEditImage: false,
        },
        plantData: {
          plantId, 
          isWatered,
          isFertilized,
          isRepotted,
          checkDate: date,
        },
        plantId: plantId,
        date: date,
      },
    });
  };


  const handleDelete = async () => {
    try {
      const diaryResponse = await API.delete(`/user/diary/${plantDiaryId}`);

      if (diaryResponse.status !== 200) {
        throw new Error('일지 삭제에 실패했습니다.');
      }
      
      const checkResponse = await API.delete(`/user/plant/${plantId}/check`, {
        params: { checkDate: date }
      });
  
      if (checkResponse.status !== 200) {
        throw new Error('식물 관리 기록 삭제에 실패했습니다.');
      }

      alert('일지가 삭제되었습니다.');
      navigate(`/plant/${plantId}`);
    } catch (error) {
      console.error('Error:', error);
      alert('일지 삭제 중 오류가 발생했습니다.');
    }
  };

  const weatherContent = `날씨는 ${weather}이고 온도는 ${temperature}'C 이며 습도는 ${humidity}입니다.`;

  const handleCapture = async () => {
    if (reportRef.current) {
      try {
        const canvas = await html2canvas(reportRef.current);
        const diaryImgData = canvas.toDataURL('image/png');
  
        // base64 문자열을 Blob으로 변환
        const byteString = atob(diaryImgData.split(',')[1]);
        const mimeString = diaryImgData.split(',')[0].split(':')[1].split(';')[0];
        const ab = new ArrayBuffer(byteString.length);
        const ia = new Uint8Array(ab);
        for (let i = 0; i < byteString.length; i++) {
          ia[i] = byteString.charCodeAt(i);
        }
        const blob = new Blob([ab], { type: mimeString });
        const file = new File([blob], 'report.png', { type: mimeString });
  
        // 파일 객체를 배열로 감싸서 `navigate`로 전달
        navigate('/sns/write', { state: { diaryImgData: [{ url: URL.createObjectURL(file), file }], articleId: 0 } });
      } catch (error) {
        console.error('이미지 캡처 중 오류 발생:', error);
      }
    }
  };
  

  return (
    <div className="plant-diary-container" ref={reportRef}>
      <div className="plant-diary-section">
        <h2>{date}</h2>
        <DiaryTodoIcon src={pencilIcon} onClick={handleEdit} />
        <Btn content="X" onClick={() => navigate(`/plant/${plantId}`)} /> {/* 이 부분은 X 를 클릭하면 PlantDetail 페이지로 돌아가야함 */}
      </div>
      <div className="plant-diary-section">
        <ImageSlider imgs={images.map(img => img)} />
      </div>
      <div className="plant-diary-section">
        {(!isWatered && !isFertilized && !isRepotted) ? (
          <div>식물관리내역이 없습니다.</div>
        ) : (
          <div>
            {isWatered && (
              <div>
                <DiaryTodoIcon src={waterIcon} /> 물주기 완료!
              </div>
            )}
            {isFertilized && (
              <div>
                <DiaryTodoIcon src={fertilizedIcon} /> 영양제주기 완료!
              </div>
            )}
            {isRepotted && (
              <div>
                <DiaryTodoIcon src={repottedIcon} /> 분갈이 완료!
              </div>
            )}
          </div>
        )}
      </div>
      <div className="plant-diary-section">
        <div className="plant-diary-todo-icons">
          <DiaryTodoIcon src={weatherIcon} />
          <DiaryTodoIcon src={humidityIcon} />
          <DiaryTodoIcon src={temperatureIcon} />
        </div>
        <DiaryWeather
          weather={weather}
          temperature={temperature}
          humidity={humidity}
          content={weatherContent}
        />
      </div>
      <div className="plant-diary-section">
        <DiaryDetailContent detailContent={content} />
      </div>
      <div>
        <Btn content="삭제하기" onClick={handleDelete} /> 
        <Btn content="SNS 업로드" onClick={handleCapture} /> 
      </div>
    </div>
  );
};

export default PlantDiaryDetail;
