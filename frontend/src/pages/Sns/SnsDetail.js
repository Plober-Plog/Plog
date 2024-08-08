import React from "react";
import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import API from "../../apis/api";

const SnsDetail = () => {
  const location = useLocation();
  // const { articleId } = location.state;
  const articleId = 50; // 임시 번호

  const [article, setArticle] = useState(null);
  const [userInfo, setUserInfo] = useState(null);


  useEffect(() => {
    const fetchArticle = async () => {
      try {
        const response = await API.get(`/user/sns/${articleId}`);
        console.log('게시글 정보:', response.data);
        setArticle(response.data)

      } catch (err) {
        console.error('게시물 불러오기 실패 : ', err);
      }
    };
    fetchArticle();
  }, [articleId]);


  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await API.get(`/user/profile/${article.searchId}`);
        console.log('유저 정보:', response.data);
        setUserInfo(response.data);
        
      } catch (err) {
        console.error('유저 정보 불러오기 실�� : ', err);
      }
    }

    fetchUser();

  }, [article])


  if (!article) {
    return <div>Loading...</div>; // 데이터를 가져오는 동안 로딩 상태를 표시
  }

  return (
    <div>
      <img src='' alt="게시글 이미지"/>
      <div>
        <p>{article.content}</p>
      </div>

    </div>
  );
}



export default SnsDetail;