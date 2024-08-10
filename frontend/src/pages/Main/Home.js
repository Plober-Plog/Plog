import React, { useEffect, useState } from "react";
import ArticleCardList from "../../components/Article/ArticleCardList";
import Img from "../../components/Common/Img";
import bannerImg from '../../assets/image/banner.png'; 
import API from '../../apis/api';

const Home = () => {

  const [diaryArticles, setDiaryArticles] = useState([]);
  const [snsArticles, setSnsArticles] = useState([]);

  useEffect(() => {
    const fetchSnsArticles = async () => {
      try {
        const response = await API.get(`/user/sns/top5`, {
          params: {
            orderType: 1
          }
        });
        console.log("SNS Articles:", response.data);
        setSnsArticles(response.data);
      } catch (error) {
        console.error("CardList Error:", error.response.data);
      }
    };

    fetchSnsArticles();
  }, []);

  return (
    <div>
      <h2>일지 SNS 발견하기</h2>
      <ArticleCardList articles={diaryArticles} />
      <Img src={bannerImg} alt="Banner Image" style={{ width: '100%', height: 'auto' }} />
      <h2>인기 SNS 발견하기</h2>
      <ArticleCardList articles={snsArticles} type="sns" />
    </div>
  )
}

export default Home;
