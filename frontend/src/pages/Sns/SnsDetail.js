import React, { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import API from "../../apis/api";

import WriterInfo from "../../components/Common/WriterInfo";
import Comment from '../../components/Sns/Comment';
import Tags from "../../components/Sns/Tags";
import ImgSlider from "../../components/Common/ImgSlider";
import BtnList from "../../components/Sns/BtnList";
import Btn from "../../components/Common/Btn";

import useAuthStore from "../../stores/member";

const SnsDetail = () => {
  const navigate = useNavigate();
  const { articleId } = useParams()
  const [article, setArticle] = useState({});
  const [writerInfo, setWriterInfo] = useState({});

  const { userData } = useAuthStore();

  useEffect(() => {
    if (!articleId) {
      console.error('@@@Invalid access: No articleId found.');
      navigate('/sns');
      return;
    }

    const fetchArticle = async () => {
      try {
        const response = await API.get(`/user/sns/${articleId}`);
        console.log('@@@게시글 정보:', response.data);
        setArticle(response.data);
        console.log('응?')
        const writerInfo = {
          imgSrc: response.data.profile,
          recordDate: response.data.createdAt.slice(0, 10),
          nickname: response.data.nickname,
        }
        setWriterInfo(writerInfo);
      } catch (err) {
        console.error('@@@게시물 불러오기 실패 : ', err);
      }
    };
    fetchArticle();
  }, [articleId]);

  if (!article || !writerInfo.imgSrc) {
    return <div>Loading...</div>;
  }

  const isAuthor = userData && userData.searchId === article.searchId;

  const handleSnsDlelete = async () => {
    try {
      const response = await API.delete(`/user/sns/${articleId}`);
      console.log('게시물 삭제 성공:', response.data);
      navigate('/sns');
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
