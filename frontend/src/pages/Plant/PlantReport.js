import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import ImgSlider from '../../components/Common/ImgSlider';
import DiaryDetailContent from '../../components/Diary/DiaryDetailContent';
import API from '../../apis/api';
import './PlantReport.css';  // CSS 파일 임포트

const PlantReport = () => {
  const location = useLocation();
  const { plantId } = location.state;

  const [plantData, setPlantData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPlantReportData = async () => {
      try {
        const response = await API.post(`/user/report/${plantId}`, {
          startDate: "2024-08-06",
          endDate: "2024-08-08"
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

  return (
    <div className="plant-report-container">
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
        <DiaryDetailContent detailContent={'얍얍얍'}/>
      </div>
    </div>
  );
};

export default PlantReport;
