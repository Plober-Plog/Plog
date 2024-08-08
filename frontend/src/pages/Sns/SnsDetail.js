import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import API from "../../apis/api";

import WriterInfo from "../../components/Common/WriterInfo";
import Comment from '../../components/Sns/Comment';
import Tags from "../../components/Sns/Tags";
import ImgSlider from "../../components/Common/ImgSlider";

const SnsDetail = () => {
  const location = useLocation();
  // const { articleId } = location.state;
  const articleId = 49;
  const [article, setArticle] = useState({});
  const [userInfo, setUserInfo] = useState({});

  console.log(article.tagTypeList)

  // 게시물 불러오기
  useEffect(() => {
    const fetchArticle = async () => {
      try {
        const response = await API.get(`/user/sns/${articleId}`);
        console.log('게시글 정보:', response.data);
        setArticle(response.data);

        // 유저 정보 가져오기
        try {
          const userResponse = await API.get(`/user/profile/${response.data.searchId}`);
          console.log('유저 정보:', userResponse.data);
          setUserInfo(userResponse.data);
        } catch (err) {
          console.error('유저 정보 불러오기 실패 : ', err);
        }
      } catch (err) {
        console.error('게시물 불러오기 실패 : ', err);
      }
    };
    fetchArticle();
  }, []);

  if (!article || !userInfo.profile) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <WriterInfo data={userInfo} type="user" />
      <ImgSlider imgs={article.images} />
      <Tags selectedTags={article.tagTypeList} tags={article.tagTypeList} />
      <p>{article.content}</p>
      <button>버튼</button>
      <Comment articleId={articleId} userInfo={userInfo} />
    </div>
  );
}

export default SnsDetail;
