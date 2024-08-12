import React, { useState, useEffect } from "react";
import { useNavigate} from 'react-router-dom';
import defaultImg from '../../assets/icon/default.png';
import './SnsCardLg.css';

const SnsCardMd = ({ articleId, nickname, image, content, likeCnt, commentCnt, isLiked, isBookmarked, createdAt, profile}) => {
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
          />
          <div className="sns-card-lg-profile-info">
            <span>{nickname}</span>
            <span>{formattedDate}</span>
          </div>
        </div>


        <p>{cutContent}</p>

        <div className="sns-card-lg-icons">
          <div className="sns-card-lg-left">
            <i>❤️:{likeCnt}</i>
            <i>💬:{commentCnt}</i>
          </div>

          <div className="sns-card-lg-right">
            <i title="좋아요">
              {isLiked ? '❤️' : '🖤'}
            </i>
            <i title="북마크">
              {isBookmarked ? '🔖' : '📕'}
            </i>
          </div>
        </div>
        
      </div>
      
    </div>
  )

}

export default SnsCardMd;