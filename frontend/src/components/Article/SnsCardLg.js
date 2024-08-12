import React, { useState, useEffect } from "react";
import { useNavigate} from 'react-router-dom';
import defaultImg from '../../assets/icon/default.png';
import './SnsCardLg.css';

const SnsCardMd = ({ articleId, nickname, image, content, likeCnt, commentCnt, isLiked, isBookmarked, createdAt, profile}) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/sns/${articleId}`, {state: { articleId }});
  }

  const cutContent = content && content.length > 50? content.slice(0, 50) + "..." : content;

  return (
    <div className="sns-card-lg" onClick={handleClick}>
      <div className="sns-card-lg-image">
        <img
          src={image || defaultImg}
          alt="thumbnail"
        />
      </div>

      <div className="sns-card-lg-content">


        <div>
          <img
            src={profile}
            alt="profile img"
          />
          <span>닉네임: {nickname}</span>
          <span>{createdAt}</span>
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