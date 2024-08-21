import React from 'react';
import ArticleCard from './ArticleCard';
<<<<<<< HEAD

const ArticleCardList = ({ plantId, articles = [], type }) => {
=======
import './ArticleCardList.css';

const ArticleCardList = ({ ownerId, articles = [], type }) => {
>>>>>>> master

  if (!Array.isArray(articles)) {
    return (
      <div>
        {articles}
      </div>
    )
  }

  if (articles.length === 0) {
    return (
      <div>
        <p>현재 보여줄 Article이 없습니다.</p>
      </div>
    )
  };

<<<<<<< HEAD
  /* TODO: BE에서 SNS 관련 api 명세를 작성한 후에 추가 및 수정할 것
    1. plantId
    2. props
      type이 'plant'가 아닌 경우, props로 다른 데이터를 보내는 것도 고려하여 수정해야 합니다.
      (식물 SNS 추천, 인기 SNS 추천 알고리즘에 쓰입니다.)
      (BE에서 큐레이팅 알고리즘을 고려해 만들어 주어야 합니다.)
  */
  return (
    <div>
      {articles.map(article => (
        <ArticleCard 
          key={article.plantDiaryId}
          plantId={plantId}
          id={article.plantDiaryId}
          log={article.recordDate}
          thumbnail={article.thumbnail}
          type={type}
        />
      ))}
=======
  return (
    <div className='article-card-list'>
      {articles.map(article => {
        if (type === 'plant') {

          const monthDay = article.recordDate ? article.recordDate.slice(5) : '';
          
          return (
            <ArticleCard 
              key={article.plantDiaryId}
              ownerId={ownerId}
              id={article.plantDiaryId}
              log={monthDay}
              recordDate={article.recordDate}
              thumbnail={article.thumbnail}
              type={type}
            />
          );
        } else if (type === 'sns') {
          return (
            <ArticleCard 
              key={article.articleId}  
              ownerId={ownerId}
              id={article.articleId}    
              log={article.nickname}     
              thumbnail={article.image} 
              type={type}
            />
          );
        } else {
          return <p>잘못된 타입: {type}</p>;
        }
      })}
>>>>>>> master
    </div>
  )
}

export default ArticleCardList;