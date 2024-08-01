import React from 'react';
import ProfileHeader from "../../components/Profile/ProfileHeader";
import Calendar from "../../components/Plant/Calendar";
import ArticleCardList from "../../components/Article/ArticleCardList";
import ReportBanner from "../../components/Plant/ReportBanner";
import Btn from "../../components/Common/Btn";
// import './PlantDetail.css';
import defaultImg from '../../assets/icon/default.png';


const PlantDetail = () => {

  // FE 체크를 위한 더미 데이터
  // 이후 API 만들어지면 수정할 것
  const plantData = {
    profile: defaultImg,
    plantTypeId: "튤립",
    nickname: "조이",
    bio: "행복한 조이"
  }

  // FE 체크를 위한 더미 데이터
  // 이후 API 만들어지면 수정할 것
  const articles = [
    { id: 1, log: "24.07.01", thumbnail: defaultImg },
    { id: 2, log: "24.07.31", thumbnail: defaultImg }
  ];

  return (
    <div>
      <ProfileHeader
        data={plantData}
        type="plant"
      />
      <Calendar />
      <ArticleCardList 
        articles={articles}
        type="plant"
      />
      <ReportBanner />
      {/* /plant/{plantTypeId}/panorama */}
      <Btn content ="성장과정 보기"/>
      {/* /plant/guide/{plantTypeId} */}
      <Btn content ="가이드"/>
    </div>
  )
};

export default PlantDetail;