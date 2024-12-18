import React, { useState, useEffect } from "react";
import { useNavigate} from 'react-router-dom';
import defaultImg from '../../assets/icon/default.png';
import './SnsCardMd.css'

import cmtIcon from '../../assets/icon/cmt-select.png';
import likeSelectIcon from '../../assets/icon/like-select-org.png';
import likeIcon from '../../assets/icon/like-default.png';
import bmkIcon from '../../assets/icon/bmk-default.png';
import bmkSelectIcon from '../../assets/icon/bmk-select.png';


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
          <span className="sns-card-md-icon">
            {isLiked ? <img className="mr-1" src={likeSelectIcon}/> : <img className="mr-1" src={likeIcon}/>} {likeCnt}
          </span>
          <span className="sns-card-md-icon">
            <img className="mr-1" src={cmtIcon}/> {commentCnt}
          </span>
          <span className="sns-card-md-icon">
            {isBookmarked ? <img className="mr-1" src={bmkSelectIcon}/> : <img className="mr-1" src={bmkIcon}/>}
          </span>
        </div>
      </div>
    </div>
  )

}

export default SnsCardMd;