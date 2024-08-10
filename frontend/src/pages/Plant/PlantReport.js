import React, { useState, useEffect, useRef } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import ImgSlider from '../../components/Common/ImgSlider';
import DiaryDetailContent from '../../components/Diary/DiaryDetailContent';
import html2canvas from 'html2canvas';
import API from '../../apis/api';
import './PlantReport.css'; 
import Btn from '../../components/Common/Btn';

const PlantReport = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { plantId } = location.state;
  const reportRef = useRef(null);
  const [plantData, setPlantData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPlantReportData = async () => {
      try {
        const response = await API.get(`/user/report/${plantId}`, {
          plantId: plantId,
        });

        console.log(response.data);
        setPlantData(response.data);
        setLoading(false);
      } catch (error) {
        setLoading(false);
      }
    };

    fetchPlantReportData();
  }, [plantId]);

  if (loading) return <p>로딩중입니당..</p>;

  if (!plantData) return <p>데이터가 없습니다.</p>;

  // 이미지 배열 생성
  const imageUrls = [plantData.firstDayImageUrl, plantData.recentImageUrl];

  const reportContent = `기간 동안 물 준 횟수 ${plantData.fertilizeData}번 💧\n 
  기간 동안 영양제 준 횟수 ${plantData.fertilizeData}번💊\n
  기간 동안 분갈이 횟수 ${plantData.repotData}번🌱\n
  \n
  물주기 점수는 "${plantData.waterResult}"\n
  영양제 점수는 "${plantData.fertilizeResult}"\n
  분갈이 점수는 "${plantData.repoResult}"\n 
  \n
  앞으로도 ${plantData.plantName} 잘 돌봐주실 거죠 😊?\n `

  const handleCapture = async () => {
    if (reportRef.current) {
      const canvas = await html2canvas(reportRef.current);
      const imgData = canvas.toDataURL('image/png');
      navigate('/sns/write', { state: { imgData, articleId : 0 } });
    }
  };


  return (
    <div className="plant-report-container" ref={reportRef}>
      <div className="plant-report-title">
        <h2>{plantData.plantName} 분석보고서</h2>
      </div>
      <div className="plant-report-slider">
        <ImgSlider imgs={imageUrls} />
      </div>
      <div className="plant-report-name">
        <p>식물 이름: {plantData.plantName}</p>
      </div>
      <div className="plant-report-details">
        <DiaryDetailContent detailContent={reportContent}/>
      </div>
      <div>
        <Btn content='SNS 공유하기' onClick={handleCapture} /> 
      </div>
    </div>
  );
};

export default PlantReport;
