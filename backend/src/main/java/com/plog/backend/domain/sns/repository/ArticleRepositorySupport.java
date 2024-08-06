package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.sns.dto.response.ArticleGetSimpleResponseDto;
import com.plog.backend.domain.sns.entity.Article;
import com.plog.backend.domain.sns.entity.QArticle;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final int size = 10;

    public ArticleRepositorySupport(JPAQueryFactory queryFactory) {
        super(Article.class);
        this.queryFactory = queryFactory;
    }

    public List<Article> loadArticleList(int page) {
        QArticle article = QArticle.article;

        return queryFactory.selectFrom(article)
                .offset(page * size)
                .limit(size)
                .fetch();
    }
}