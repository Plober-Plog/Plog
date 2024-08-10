import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../../pages/Plant/PlantDetail.css';
import '../../../src/output.css';
import defaultImg from '../../assets/icon/default.png';

/* TODO: BE에서 SNS 관련 api 명세를 작성한 후에 추가 및 수정할 것
  type이 'plant'가 아닌 경우, props로 다른 데이터를 보내는 것도 고려하여 수정해야 합니다.
  (식물 SNS 추천, 인기 SNS 추천 알고리즘에 쓰입니다.)
  (BE에서 큐레이팅 알고리즘을 고려해 만들어 주어야 합니다.)
*/
const ArticleCard = ({ ownerId, id, log, thumbnail, type }) => {

  const navigate = useNavigate();

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
      console.log(`type error${type}`);
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