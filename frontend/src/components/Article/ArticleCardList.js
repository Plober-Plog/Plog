import React from 'react';
import ArticleCard from './ArticleCard';

// data = {}, || ''
// 위 부분은 아직 api가 없어 전달할 데이터가 존재하지 않기 떄문에 추가한 내용
// api가 만들어지면, 추후 확인 후 수정할 것
// 수정 시 data가 없거나 잘못 들어온 경우를 고려해야 함을 유의하기

const ArticleCardList = ({ articles = [], type }) => {
  if (articles.length === 0) {
    return (
      <div>
        <p>현재 보여줄 Article이 없습니다.</p>
      </div>
    )
  };
  
  return (
    <div>
      {articles.map(article => (
        <ArticleCard 
          key={article.id}
          id={article.id}
          log={article.log}
          thumbnail={article.thumbnail}
          type={type}
        />
      ))}
    </div>
  )
}

export default ArticleCardList;