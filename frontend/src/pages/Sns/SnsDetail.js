import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import API from "../../apis/api";

import WriterInfo from "../../components/Common/WriterInfo";
import Comment from '../../components/Sns/Comment';
import Tags from "../../components/Sns/Tags";
import ImgSlider from "../../components/Common/ImgSlider";
import BtnList from "../../components/Sns/BtnList";
import Btn from "../../components/Common/Btn";

import useAuthStore from "../../stores/member";

const SnsDetail = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { articleId } = location.state;
  const [article, setArticle] = useState({});
  const [writerInfo, setWriterInfo] = useState({});

  const { userData } = useAuthStore();

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
          setWriterInfo(userResponse.data);
        } catch (err) {
          console.error('유저 정보 불러오기 실패 : ', err);
        }
      } catch (err) {
        console.error('게시물 불러오기 실패 : ', err);
      }
    };
    fetchArticle();
  }, [articleId]);

  if (!article || !writerInfo.profile) {
    return <div>Loading...</div>;
  }

  // 현재 사용자가 게시물 작성자인지 확인
  // TODO userId로 확인
  const isAuthor = userData && userData.searchId === article.searchId;

  // 게시물 삭제하기
  const handleSnsDlelete = async () => {
    try {
      const response = await API.delete(`/user/sns/${articleId}`);
      console.log('게시물 삭제 성공:', response.data);
      navigate('/sns'); // 삭제 후 SNS 목록 페이지로 이동
    } catch (err) {
      console.error('게시물 삭제 실패 : ', err);
    }
  }

  return (
    <div>
      <WriterInfo data={writerInfo} type="user" onClick={() => navigate(`/profile/${writerInfo.searchId}`)}/>
      {isAuthor && (
        <>
          <Btn content='수정하기' onClick={() => navigate('/sns/write', { state: { articleId } })} />
          <Btn content='삭제하기' onClick={() => handleSnsDlelete()} />
        </>
      )}
      <ImgSlider imgs={article.images} />
      <Tags selectedTags={article.tagTypeList} tags={article.tagTypeList} />
      <p>{article.content}</p>
      <BtnList likeCnt={article.likeCnt} isLiked={article.isLiked} commentCnt={article.commentCnt} isBookmarked={article.isBookmarked} articleId={articleId} />
      <Comment articleId={articleId}/>
    </div>
  );
}

export default SnsDetail;
