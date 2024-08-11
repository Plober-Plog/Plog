import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../../pages/Plant/PlantDetail.css';
import '../../../src/output.css';
import defaultImg from '../../assets/icon/default.png';

const ArticleCard = ({ ownerId, id, log, thumbnail, type }) => {

  const navigate = useNavigate();
  // console.log('Article Card의 id값 콘솔***', id)
  const handleClick = () => {
    if (type === 'plant') {
      navigate(`/plant/${ownerId}/${log}`, {
        state: {
          date: log,
          plantId: ownerId
        }
      });

    } else if (type === 'sns') {
      navigate(`/sns/${id}`, {
        state: {
          articleId: id,
        }
      });
    } else {
      console.log(`type error ${type}`);
    }
  };

  return (
    <div onClick={handleClick}>
      <img src={thumbnail || defaultImg} alt="thumbnail" className='article-thumbnail'/>
      <p>{log}</p>
    </div>
  )
}

export default ArticleCard;