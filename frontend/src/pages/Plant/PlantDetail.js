import React, { useEffect, useState } from 'react';
import ProfileHeader from "../../components/Profile/ProfileHeader";
import Calendar from "../../components/Plant/Calendar";
import ArticleCardList from "../../components/Article/ArticleCardList";
import ReportBanner from "../../components/Plant/ReportBanner";
import Btn from "../../components/Common/Btn";
// import './PlantDetail.css';
import defaultImg from '../../assets/icon/default.png';

/* TODO: 이 전의 페이지가 업데이트 되면 하드코딩 부분 수정할 것 */
const PlantDetail = ({ plantId = 1 }) => {

  const URI = 'https://i11b308.p.ssafy.io/api'

  const [plantData, setPlantData] = useState(null);
  const [articles, setArticles] = useState([]);

  useEffect(() => {
    const fetchPlantData = async () => {
      try {
        const response = await fetch(`${URI}/user/plant/${plantId}/info`);
        const data = await response.json();
        setPlantData(data);
      } catch (error) {
        console.error("PlantData Error:", error);
      }
    };

    const fetchArticles = async () => {
      try {
        const response = await fetch(`${URI}/user/plant/${plantId}/diary`);
        const data = await response.json();
        setArticles(data);
      } catch (error) {
        console.error("CardList Error:", error)
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
        data={plantData}
        type="plant"
      />
      <Calendar 
        plantId={plantId}
      />
      <ArticleCardList 
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