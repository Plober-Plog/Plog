import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../../pages/Plant/PlantDetail.css';
import '../../../src/output.css';

const ArticleCard = ({ id, log, thumbnail, type }) => {

  const navigate = useNavigate();

  const handleClick = () => {
    if (type === 'plant') {
      navigate(`/plant/diary/${id}`);
    } else if (type === 'sns') {
      navigate(`/sns/${id}`);
    } else {
      console.log(`type error${type}`);
    }
  };

  return (
    <div onClick={handleClick}>
      <img src={thumbnail} alt="thumbnail" className='article-thumbnail'/>
      <p>{log}</p>
    </div>
  )
}

export default ArticleCard;