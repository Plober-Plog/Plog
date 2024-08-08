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

  useEffect(() => {
    const fetchPlantData = async () => {
      try {
        const response = await API.get(`/user/plant/${plantId}/info`);
        setPlantData(response.data);
        setPlantTypeId(response.data.plantTypeId);
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
        data={{ ...plantData, ownerId:plantId }}
        type="plant"
      />
      <CustomCalendar 
        plantId={plantId}
      />
      <ArticleCardList 
        ownerId={plantId}
        articles={articles}
        type="plant"
      />
      <ReportBanner />
      <Btn content ="성장과정 보기"/>
      <Btn content ="가이드" onClick={handleGuideClick}/>
    </div>
  )
};

export default PlantDetail;