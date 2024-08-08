import React from "react";
import ArticleCardList from "../../components/Article/ArticleCardList";
import Img from "../../components/Common/Img";
import bannerImg from '../../assets/image/banner.png'; 

const Home = () => {

  return (
    <div>
      <h2>식물 SNS 발견하기</h2>
      <ArticleCardList />
      <Img src={bannerImg} alt="Banner Image" style={{ width: '100%', height: 'auto' }} />
      <ArticleCardList />
    </div>
  )
}

export default Home;