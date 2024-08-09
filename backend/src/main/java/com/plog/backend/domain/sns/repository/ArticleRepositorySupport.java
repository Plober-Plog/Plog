// ArticleRepositorySupport.java

package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.neighbor.entity.QNeighbor;
import com.plog.backend.domain.sns.entity.Article;
import com.plog.backend.domain.sns.entity.QArticle;
import com.plog.backend.domain.sns.entity.QArticleTag;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ArticleRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final int size = 10;

    public ArticleRepositorySupport(JPAQueryFactory queryFactory) {
        super(Article.class);
        this.queryFactory = queryFactory;
    }

    public List<Article> loadArticleList(int page, String searchId, List<Integer> tagTypeList, String keyword, long userId, int neighborType) {
        QArticle article = QArticle.article;
        QArticleTag articleTag = QArticleTag.articleTag;
        QNeighbor neighbor = QNeighbor.neighbor;

        List<Article> articleList = queryFactory.selectFrom(article)
                .leftJoin(articleTag).on(article.articleId.eq(articleTag.article.articleId))
                .leftJoin(neighbor).on(neighbor.neighborTo.userId.eq(article.user.userId)
                        .or(neighbor.neighborFrom.userId.eq(article.user.userId)))
                .where(
                        eqSearchId(searchId),
                        containsKeyword(keyword),
                        inTagTypeList(tagTypeList),
                        filterByNeighborType(userId, neighborType),
                        article.state.eq(1) // PLAIN
                )
                .fetch();

        List<Article> filteredArticleList = articleList.stream()
                .sorted(Comparator.comparing(Article::getCreatedAt)) // 오름차순 정렬
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toList());


        return filteredArticleList;
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

    private BooleanExpression filterByNeighborType(long userId, int neighborType) {
        QNeighbor neighbor = QNeighbor.neighbor;

        if (neighborType == 0) {
            return null; // 이웃 관계 없이 모든 게시글 조회
        } else if (neighborType == 1) {
            return neighbor.neighborFrom.userId.eq(userId).and(neighbor.neighborType.in(1, 2))
                    .or(neighbor.neighborTo.userId.eq(userId).and(neighbor.neighborType.in(1, 2)));
        } else if (neighborType == 2) {
            return neighbor.neighborFrom.userId.eq(userId).and(neighbor.neighborType.eq(2))
                    .or(neighbor.neighborTo.userId.eq(userId).and(neighbor.neighborType.eq(2)));
        } else {
            return null; // 잘못된 neighborType인 경우 모든 게시글 조회
        }
    }
}
