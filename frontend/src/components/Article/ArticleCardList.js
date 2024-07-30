import React from 'react';
import ArticleCard from './ArticleCard';

// 아래 부분은 아직 api가 없어 전달할 데이터가 없기 때문에 추가한 내용이니
// 추후 확인 후 수정 혹은 삭제할 것
// articles = {},
// || '',

const ArticleCardList = ({ articles = [] }) => {
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
                />
            ))}
        </div>
    )
}

export default ArticleCardList;