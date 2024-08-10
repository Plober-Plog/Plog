import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../../apis/api";

import bookMarkSelect from "../../assets/icon/bookmark-select.svg";
import bookMarkDefault from "../../assets/icon/bookmark-default.svg";
import likeSelect from "../../assets/icon/like-select.svg";
import likeDefault from "../../assets/icon/like-default.svg";
import comment from "../../assets/icon/comment.svg";

const BtnList = ({ likeCnt: initialLikeCnt, isLiked: initialIsLiked, commentCnt, isBookmarked: initialIsBookmarked, articleId }) => {
  const navigate = useNavigate();
  const [likeCnt, setLikeCnt] = useState(initialLikeCnt);
  const [isLiked, setIsLiked] = useState(initialIsLiked);
  const [isBookmarked, setIsBookmarked] = useState(initialIsBookmarked);
  const imgStyle = { width: '24px', height: '24px' };

  const handleLike = async () => {
    try {
      const response = isLiked
        ? await API.delete(`/user/sns/like/${articleId}`)
        : await API.post(`/user/sns/like/${articleId}`);

      setIsLiked(!isLiked);
      setLikeCnt(prevLikeCnt => isLiked ? prevLikeCnt - 1 : prevLikeCnt + 1);
      console.log('좋아요 성공:', response.data);
    } catch (error) {
      console.error('좋아요 실패:', error.response);
    }
  };

  const handleBookmark = async () => {
    try {
      const response = isBookmarked
        ? await API.delete(`/user/sns/bookmark/${articleId}`)
        : await API.post(`/user/sns/bookmark/${articleId}`);

      setIsBookmarked(!isBookmarked);
      console.log('북마크 성공:', response.data);
    } catch (error) {
      console.error('북마크 실패:', error.response);
    }
  };

  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
        <button onClick={handleLike} style={{ background: 'none', border: 'none', padding: 0 }}>
          <img src={isLiked ? likeSelect : likeDefault} alt="like" style={imgStyle} />
        </button>
        <span>{likeCnt}</span>
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
        <img 
          src={comment} 
          alt="comment" 
          style={imgStyle} 
          onClick={() => navigate(`/sns/${articleId}/comment`, { state: { articleId }})} // navigate를 함수로 감싸기
        />
        <span>{commentCnt}</span>
      </div>
      <div>
        <button onClick={handleBookmark} style={{ background: 'none', border: 'none', padding: 0 }}>
          <img src={isBookmarked ? bookMarkSelect : bookMarkDefault} alt="bookmark" style={imgStyle} />
        </button>
      </div>
    </div>
  );
}

export default BtnList;
