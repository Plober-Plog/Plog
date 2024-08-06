import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

import ProfileHeader from "../../components/Profile/ProfileHeader";
import CustomCalendar from "../../components/Plant/Calendar";
import ArticleCardList from "../../components/Article/ArticleCardList";
import ReportBanner from "../../components/Plant/ReportBanner";
import Btn from "../../components/Common/Btn";
// import './PlantDetail.css';
import axios from 'axios';
import defaultImg from '../../assets/icon/default.png';

/* TODO: 이 전의 페이지가 업데이트 되면 하드코딩 부분 수정할 것 */
const PlantDetail = () => {

  const { plantId } = useParams();

  const URI = 'https://i11b308.p.ssafy.io/api'
  const TOKEN = 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoicGxvZy5jb20iLCJleHAiOjE3MjQwNDg3MDYsImlhdCI6MTcyMjgzOTEwNn0.zyGGYRJrG4SELAACBabt-AiBKPOC_TvVsBZdrk8IfZQ'

  const [plantData, setPlantData] = useState(null);
  const [articles, setArticles] = useState([]);

  useEffect(() => {
    const fetchPlantData = async () => {
      try {
        const response = await axios.get(`${URI}/user/plant/${plantId}/info`);
        setPlantData(response.data);
      } catch (error) {
        console.error("PlantData Error:", error.response.data);
      }
    };

    const fetchArticles = async () => {
      try {
        const response = await axios.get(`${URI}/user/plant/${plantId}/diary`);
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
      <Btn content ="가이드"/>
    </div>
  )
};

export default PlantDetail;