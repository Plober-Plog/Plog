import React, { useState, useEffect } from "react";
import { useNavigate} from 'react-router-dom';
import defaultImg from '../../assets/icon/default.png';
import './SnsCardMd.css'

const SnsCardMd = ({ articleId, nickname, image, content, likeCnt, commentCnt, isLiked, isBookmarked}) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/sns/${articleId}`, {state: { articleId }});
  }

  const cutContent = content && content.length > 30? content.slice(0, 30) + "..." : content;

  return (
    <div className="sns-card-md" onClick={handleClick}>
      <div className="sns-card-md-thumbnail">
        <img
          src={image || defaultImg}
          alt="thumbnail"
        />
      </div>

      <div className="sns-card-md-content">
        <p className="sns-card-md-text">{cutContent}</p>

        <div className="sns-card-md-icons">
          <span className="sns-card-md-icon">{isLiked ? '❤️' : '🖤'} {likeCnt}</span>
          <span className="sns-card-md-icon">💬 {commentCnt}</span>
          <span className="sns-card-md-icon">{isBookmarked ? '🔖' : ''}</span>
        </div>
      </div>
    </div>
  )

}

export default SnsCardMd;