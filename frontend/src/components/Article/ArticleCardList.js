import React from 'react';
import ArticleCard from './ArticleCard';

const ArticleCardList = ({ articles = [], type }) => {
  if (articles.length === 0) {
    return (
      <div>
        <p>현재 보여줄 Article이 없습니다.</p>
      </div>
    )
  };

  /* TODO: BE에서 SNS 관련 api 명세를 작성한 후에 추가 및 수정할 것
    type이 'plant'가 아닌 경우, props로 다른 데이터를 보내는 것도 고려하여 수정해야 합니다.
    (식물 SNS 추천, 인기 SNS 추천 알고리즘에 쓰입니다.)
    (BE에서 큐레이팅 알고리즘을 고려해 만들어 주어야 합니다.)
  */
  return (
    <div>
      {articles.map(article => (
        <ArticleCard 
          key={article.plantDiaryId}
          id={article.plantDiaryId}
          log={article.recordDate}
          thumbnail={article.thumbnail}
          type={type}
        />
      ))}
    </div>
  )
}

export default ArticleCardList;