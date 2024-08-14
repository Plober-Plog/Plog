import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../../apis/api";
import useAuthStore from '../../stores/member';

// import bookMarkSelect from "../../assets/icon/bookmark-select.svg";
// import bookMarkDefault from "../../assets/icon/bookmark-default.svg";
// import likeSelect from "../../assets/icon/like-select.svg";
// import likeDefault from "../../assets/icon/like-default.svg";
// import comment from "../../assets/icon/comment.svg";

import comment from '../../assets/icon/cmt-select.png';
import likeSelect from '../../assets/icon/like-select-org.png';
import likeDefault from '../../assets/icon/like-default.png';
import bookMarkDefault from '../../assets/icon/bmk-default.png';
import bookMarkSelect from '../../assets/icon/bmk-select.png';

import './BtnList.css';

const BtnList = ({ likeCnt: initialLikeCnt, isLiked: initialIsLiked, commentCnt, isBookmarked: initialIsBookmarked, articleId }) => {
  const navigate = useNavigate();
  const { isLogin } = useAuthStore();
  const [likeCnt, setLikeCnt] = useState(initialLikeCnt);
  const [isLiked, setIsLiked] = useState(initialIsLiked);
  const [isBookmarked, setIsBookmarked] = useState(initialIsBookmarked);
  const imgStyle = { width: '24px', height: '24px' };

  const handleLike = async () => {
    if (isLogin) {
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
    }
  };

  const handleBookmark = async () => {
    if (isLogin) {
      try {
        const response = isBookmarked
          ? await API.delete(`/user/sns/bookmark/${articleId}`)
          : await API.post(`/user/sns/bookmark/${articleId}`);
  
        setIsBookmarked(!isBookmarked);
        console.log('북마크 성공:', response.data);
      } catch (error) {
        console.error('북마크 실패:', error.response);
      }
    }
  };

  return (
    <div className="action-container">
      <div>
        <button onClick={handleLike}>
          <img src={isLiked ? likeSelect : likeDefault} alt="like" />
        </button>
        <span>{likeCnt}</span>
      </div>
      <div>
        <img 
          src={comment} 
          alt="comment" 
          onClick={() => navigate(`/sns/${articleId}/comment`, { state: { articleId }})}
        />
        <span>{commentCnt}</span>
      </div>
      <div>
        <button onClick={handleBookmark}>
          <img src={isBookmarked ? bookMarkSelect : bookMarkDefault} alt="bookmark" />
        </button>
      </div>
    </div>
    // <div className="action-container">
    //   <div>
    //     <button onClick={handleLike}>
    //       <img src={isLiked ? likeSelect : likeDefault} alt="like" />
    //     </button>
    //     <span>{likeCnt}</span>
    //   </div>
    //   <div>
    //     <img 
    //       src={comment} 
    //       alt="comment" 
    //       onClick={() => navigate(`/sns/${articleId}/comment`, { state: { articleId }})} // navigate를 함수로 감싸기
    //     />
    //     <span>{commentCnt}</span>
    //   </div>
    //   <div>
    //     <button onClick={handleBookmark}>
    //       <img src={isBookmarked ? bookMarkSelect : bookMarkDefault} alt="bookmark" />
    //     </button>
    //   </div>
    // </div>
  );
}

export default BtnList;
