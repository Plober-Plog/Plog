package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.sns.dto.response.ArticleGetSimpleResponseDto;
import com.plog.backend.domain.sns.entity.Article;
import com.plog.backend.domain.sns.entity.QArticle;
import com.plog.backend.domain.sns.entity.QArticleTag;
import com.plog.backend.domain.user.entity.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
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

    public List<Article> loadArticleList(int page, String searchId, List<Integer> tagTypeList, String keyword) {
        QArticle article = QArticle.article;
        QArticleTag articleTag = QArticleTag.articleTag;

        return queryFactory.selectFrom(article)
                .leftJoin(articleTag).on(article.articleId.eq(articleTag.article.articleId))
                .where(
                        eqSearchId(searchId),
                        containsKeyword(keyword),
                        inTagTypeList(tagTypeList),
                        article.state.eq(1) // PLAIN
                )
                .offset(page * size)
                .limit(size)
                .fetch();
    }

    private BooleanExpression eqSearchId(String searchId) {
        return searchId != null ? QArticle.article.user.searchId.eq(searchId) : null;
    }

    private BooleanExpression containsKeyword(String keyword) {
        return keyword != null && !keyword.isEmpty() ? QArticle.article.content.containsIgnoreCase(keyword) : null;
    }

    private BooleanExpression inTagTypeList(List<Integer> tagTypeList) {
        return (tagTypeList != null && !tagTypeList.isEmpty()) ? QArticleTag.articleTag.tagType.tagTypeId.in(tagTypeList) : null;
    }
}