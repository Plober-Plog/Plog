import React, { useState, useEffect } from "react";
import { useNavigate} from 'react-router-dom';
import defaultImg from '../../assets/icon/default.png';

const SnsCardMd = ({ articleId, nickname, image, content, likeCnt, commentCnt, isLiked, isBookmarked}) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/sns/${articleId}`, {state: { articleId }});
  }

  const cutContent = content && content.length > 50? content.slice(0, 50) + "..." : content;

  return (
    <div onClick={handleClick}>
      <img
        src={image || defaultImg}
        alt="thumbnail"
      />
      <div>
        <span>닉네임: {nickname}</span>
      </div>
      <p>{cutContent}</p>
      <p>좋아요 수:{likeCnt}</p>
      <p>댓글 수:{commentCnt}</p>
      <i title="좋아요">
        {isLiked ? '❤️' : '🖤'}
      </i>
      <i title="북마크">
      {isBookmarked ? '🔖' : '📕'}
      </i>
    </div>
  )

}

export default SnsCardMd;