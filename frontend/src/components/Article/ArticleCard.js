import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../../pages/Plant/PlantDetail.css';

const ArticleCard = ({ id, log, thumbnail, type }) => {

  const navigate = useNavigate();

  const handleClick = () => {
    if (type === 'plant') {
      navigate(`/api/user/diary/${id}`);
    } else if (type === 'sns') {
      navigate(`/api/user/sns/${id}`);
    } else {
      console.log(`type error${type}`);
    }
  };

  return (
    <div onClick={handleClick} className='article-card'>
      <img src={thumbnail} alt="thumbnail" className='article-thumbnail'/>
      <p>{log}</p>
    </div>
  )
}

export default ArticleCard;