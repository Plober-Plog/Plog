import React from 'react';
import { useNavigate } from 'react-router-dom';

const ArticleCard = ({ id, log, thumbnail, type }) => {

  const navigate = useNavigate();

  const handleClick = () => {
    if (type === 'plant') {
      navigate(`/api/user/diary/${id}`);
    } else if (type === 'sns') {
      navigate(`/api/user/sns/${id}`);
    }
  };

  return (
    <div onClick={handleClick}>
      <img src={thumbnail} alt="thumbnail" />
      <p>{log}</p>
    </div>
  )
}

export default ArticleCard;