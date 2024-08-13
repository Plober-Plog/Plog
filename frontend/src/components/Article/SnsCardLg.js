import React, { useState, useEffect } from "react";
import { useNavigate} from 'react-router-dom';
import defaultImg from '../../assets/icon/default.png';
import './SnsCardLg.css';

import cmtIcon from '../../assets/icon/cmt-select.png';
import likeSelectIcon from '../../assets/icon/like-select-org.png';
import likeIcon from '../../assets/icon/like-default.png';
import bmkIcon from '../../assets/icon/bmk-default.png';
import bmkSelectIcon from '../../assets/icon/bmk-select.png';

const SnsCardMd = ({ searchId, articleId, nickname, image, content, likeCnt, commentCnt, isLiked, isBookmarked, createdAt, profile}) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/sns/${articleId}`, {state: { articleId }});
  }

  // const cutContent = content && content.length > 50? content.slice(0, 50) + "..." : content;

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}.${month}.${day} ${hours}:${minutes}`;
  }

  const cutContent = content && content.length > 50 ? content.slice(0, 50) + "..." : content;
  const formattedDate = formatDate(createdAt);

  const handleProfileClick = (event) => {
    event.stopPropagation();
    navigate(`/profile/${searchId}`)
  }

  return (
    <div className="sns-card-lg" onClick={handleClick}>
      <div className="sns-card-lg-image">
        <img
          src={image || defaultImg}
          alt="thumbnail"
        />
      </div>

      <div className="sns-card-lg-content">

        <div className="sns-card-lg-profile">
          <img
            src={profile}
            alt="profile img"
            onClick={handleProfileClick}
          />
          <div className="sns-card-lg-profile-info">
            <span>{nickname}</span>
            <span>{formattedDate}</span>
          </div>
        </div>


        <p>{cutContent}</p>

        <div className="sns-card-lg-icons">
          
          <div className="sns-card-lg-left">
            <span className="sns-card-lg-icon mr-3 gap-1">
              <img src={likeSelectIcon} alt="좋아요 수" />
              {likeCnt}
            </span>
            <span className="sns-card-lg-icon gap-1">
              <img src={cmtIcon} alt="댓글 수 " />
              {commentCnt}
            </span>
          </div>

          <div className="sns-card-lg-right">
          <span className="sns-card-lg-icon mr-3 gap-1">
            {isLiked? <img src={likeSelectIcon} alt="좋아요on"/> : <img src={likeIcon} alt="좋아요off"/>}
          </span>
          <span className="sns-card-lg-icon gap-1">
            {isBookmarked? <img src={bmkSelectIcon} alt="북마크on"/> : <img src={bmkIcon} alt="북마크off"/>}
          </span>

          </div>
        </div>
        
      </div>
      
    </div>
  )

}

export default SnsCardMd;