import React from 'react';

// id는 눌렀을 때 detail 페이지로 연결하는 데에 씀
// 일지의 경우 일지 detail, sns의 경우 sns detail

const ArticleCard = ({ id, log, thumbnail }) => {
    return (
        <div>
            <img src={thumbnail} alt="thumbnail" />
            <p>{log}</p>
        </div>
    )
}

export default ArticleCard;