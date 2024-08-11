import React from 'react';
import ArticleCard from './ArticleCard';
import './ArticleCardList.css';

const ArticleCardList = ({ ownerId, articles = [], type }) => {

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

  return (
    <div className='article-card-list'>
      {articles.map(article => {
        if (type === 'plant') {
          return (
            <ArticleCard 
              key={article.plantDiaryId}
              ownerId={ownerId}
              id={article.plantDiaryId}
              log={article.recordDate}
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
    </div>
  )
}

export default ArticleCardList;