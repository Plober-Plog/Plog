import React, { useEffect, useState } from "react";
import ArticleCardList from "../../components/Article/ArticleCardList";
import Img from "../../components/Common/Img";
import bannerImg from '../../assets/image/banner.png'; 
import API from '../../apis/api';
import '../../styles/index.css'

const Home = () => {

  const [diaryArticles, setDiaryArticles] = useState([]);
  const [snsArticles, setSnsArticles] = useState([]);

  useEffect(() => {
    const fetchDiaryArticles = async () => {
      try {
        const diaryResponse = await API.get(`/user/sns/top5`, {
          params: {
            tagType: 1,
          }
        });
        console.log("Diary Articles:", diaryResponse.data);
        setDiaryArticles(diaryResponse.data);
      } catch (error) {
        console.error("다이어리 오류", error);
      }
    };

    const fetchSnsArticles = async () => {
      try {
        const snsResponse = await API.get(`/user/sns/top5`, {
          params: {
            orderType: 1
          }
        });
        console.log("SNS Articles:", snsResponse.data);
        setSnsArticles(snsResponse.data);
      } catch (error) {
        console.error("sns 오류:", error);
      }
    };

    fetchDiaryArticles();
    fetchSnsArticles();
  }, []);

  return (
    <div>
      <div className="pt-8">
        <p className="cardlist-subtitle">일지 SNS 발견하기</p>
        <ArticleCardList articles={diaryArticles} type="sns" />
      </div>
      
      <div className="pt-8">
        <Img src={bannerImg} alt="Banner Image" style={{ width: '100%', height: 'auto' }} />
      </div>
      
      <div className="pt-8">
        <p className="cardlist-subtitle">인기 SNS 발견하기</p>
        <ArticleCardList articles={snsArticles} type="sns" />
      </div>

    </div>
  )
}

export default Home;
