import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

import ProfileHeader from "../../components/Profile/ProfileHeader";
import CustomCalendar from "../../components/Plant/Calendar";
import ArticleCardList from "../../components/Article/ArticleCardList";
import ReportBanner from "../../components/Plant/ReportBanner";
import Btn from "../../components/Common/Btn";
// import './PlantDetail.css';
import defaultImg from '../../assets/icon/default.png';

import API from '../../apis/api';
import useAuthStore from '../../stores/member';

const PlantDetail = () => {

  const { plantId } = useParams();
  const navigate = useNavigate();

  const [plantData, setPlantData] = useState(null);
  const [articles, setArticles] = useState([]);
  const [plantTypeId, setPlantTypeId] = useState(null); //plantTypeId 받아오기
  // const [otherPlantTypeId, setOtherPlantTypeId] = useState(null);

  //etcPlantType
  const [etcPlantType, setEtcPlantType] = useState(false);

  useEffect(() => {
    const fetchPlantData = async () => {
      try {
        const response = await API.get(`/user/plant/${plantId}/info`);
        console.log('************Plant Type Name:', response.data.plantTypeName); // 추가된 로그

        const displayPlantTypeName = response.data.plantTypeName === '기타'
          ? response.data.otherPlantTypeName
          : response.data.plantTypeName;

        setPlantData({
          ...response.data,
          plantTypeName: displayPlantTypeName,
        });

        setPlantTypeId(response.data.plantTypeId);

        setEtcPlantType(response.data.plantTypeName === '기타');
      } catch (error) {
        console.error("PlantData Error:", error.response.data);
      }
    };

    const fetchArticles = async () => {
      try {
        const response = await API.get(`/user/plant/${plantId}/diary`);
        setArticles(response.data);
      } catch (error) {
        console.error("CardList Error:", error.response.data);
      }
    };

    fetchPlantData();
    fetchArticles();
  }, [plantId]);

  if (!plantData) {
    return <div>Loading</div>
  };

  const handleReportClick = () => {
    if (plantId) {
      navigate(`/plant/${plantId}/report`, { state: { plantId } });
    } else {
      console.error('plantId 를 확인할 수 없습니다');
    }
  };

  const handleGuideClick = () => {
    if (plantTypeId) {
      navigate(`/guide/${plantTypeId}`, { state: { plantTypeId } });
    } else {
      console.error('plantTypeId 를 확인할 수 없습니다');
    }
  };


  return (
    <div>
      <ProfileHeader
        data={{ ...plantData, ownerId:plantId, etcPlantType }}
        type="plant"
      />
      
      <div className='mt-8'>
        <CustomCalendar 
          plantId={plantId}
        />
      </div>

      <div className="pt-9">
        <p className="cardlist-subtitle">최근 일지 모아보기</p>
        <ArticleCardList 
          ownerId={plantId}
          articles={articles}
          type="plant"
        />
      </div>

      <div className='grid grid-cols-12 gap-3 mt-10 mb-10'>
        <div className='col-span-6'>
          <Btn content ="분석보고서" onClick={handleReportClick}/>
        </div>
        <div className='col-span-6'>
          <Btn content ="가이드" onClick={handleGuideClick}/>
        </div>
      </div>

    </div>
  )
};

export default PlantDetail;