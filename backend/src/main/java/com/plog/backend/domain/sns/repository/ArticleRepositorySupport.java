// ArticleRepositorySupport.java

package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.neighbor.entity.QNeighbor;
import com.plog.backend.domain.sns.entity.Article;
import com.plog.backend.domain.sns.entity.QArticle;
import com.plog.backend.domain.sns.entity.QArticleTag;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ArticleRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final int size = 10;
    private final ArticleLikeRepositorySupport articleLikeRepositorySupport;

    @Autowired
    public ArticleRepositorySupport(JPAQueryFactory queryFactory, ArticleLikeRepositorySupport articleLikeRepositorySupport) {
        super(Article.class);
        this.queryFactory = queryFactory;
        this.articleLikeRepositorySupport = articleLikeRepositorySupport;
    }

    public List<Article> loadArticleList(int page, String searchId, List<Integer> tagTypeList, String keyword, long userId, int neighborType, int orderType) {
        QArticle article = QArticle.article;
        QArticleTag articleTag = QArticleTag.articleTag;
        QNeighbor neighbor = QNeighbor.neighbor;

        // 모든 게시글을 먼저 불러옵니다.
        List<Article> articleList = queryFactory.selectFrom(article)
                .leftJoin(articleTag).on(article.articleId.eq(articleTag.article.articleId))
                .leftJoin(neighbor).on(neighbor.neighborTo.userId.eq(article.user.userId)
                        .or(neighbor.neighborFrom.userId.eq(article.user.userId)))
                .where(
                        eqSearchId(searchId),
                        containsKeyword(keyword),
                        inTagTypeList(tagTypeList),
                        filterByNeighborType(userId, neighborType),
                        article.state.eq(1), // PLAIN 상태인 게시글만 조회
                        filterByVisibility(userId, neighborType) // visibility 필터링 추가
                )
                .fetch();

        List<Article> filteredArticleList = articleList.stream()
                .sorted((a1, a2) -> {
                    if (orderType == 1) {
                        // 좋아요 순서로 정렬
                        return Integer.compare(
                                articleLikeRepositorySupport.countLikesByArticleId(a2.getArticleId()),
                                articleLikeRepositorySupport.countLikesByArticleId(a1.getArticleId()));
                    } else {
                        // 최신순으로 정렬 (예: 생성 날짜 기준)
                        return a2.getCreatedAt().compareTo(a1.getCreatedAt());
                    }
                })
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

        if (neighborType == 1) {
            return null; // 이웃 관계 없이 모든 게시글 조회
        } else if (neighborType == 2) {
            return neighbor.neighborFrom.userId.eq(userId).and(neighbor.neighborType.in(1, 2))
                    .or(neighbor.neighborTo.userId.eq(userId).and(neighbor.neighborType.in(1, 2)));
        } else if (neighborType == 3) {
            return neighbor.neighborFrom.userId.eq(userId).and(neighbor.neighborType.in(2))
                    .or(neighbor.neighborTo.userId.eq(userId).and(neighbor.neighborType.in(2)));
        } else {
            return null; // 잘못된 neighborType인 경우 모든 게시글 조회
        }
    }

    private BooleanExpression filterByVisibility(long userId, int neighborType) {
        QArticle article = QArticle.article;
        QNeighbor neighbor = QNeighbor.neighbor;

        BooleanExpression visibilityCondition = null;

        if (neighborType == 1) {
            // neighborType이 1인 경우 visibility가 1인 게시글만 조회
            visibilityCondition = article.visibility.eq(1);
        } else if (neighborType == 2) {
            // neighborType이 2인 경우 visibility가 2인 게시글만 조회
            visibilityCondition = article.visibility.eq(2)
                    .and(neighbor.neighborTo.user.userId.eq(userId)
                            .or(neighbor.neighborFrom.userId.eq(userId))
                            .and(neighbor.neighborType.eq(1).or(neighbor.neighborType.eq(2))));
        } else if (neighborType == 3) {
            // neighborType이 3인 경우 visibility가 3인 게시글만 조회
            visibilityCondition = article.visibility.eq(3)
                    .and(neighbor.neighborTo.user.userId.eq(userId)
                            .or(neighbor.neighborFrom.userId.eq(userId))
                            .and(neighbor.neighborType.eq(2)));
        }

        return visibilityCondition;
    }



    public List<Article> loadArticleTop5List(List<Integer> tagTypeList, int orderType) {
        QArticle article = QArticle.article;
        QArticleTag articleTag = QArticleTag.articleTag;
        QNeighbor neighbor = QNeighbor.neighbor;

        List<Article> articleList = queryFactory.selectFrom(article)
                .leftJoin(articleTag).on(article.articleId.eq(articleTag.article.articleId))
                .leftJoin(neighbor).on(neighbor.neighborTo.userId.eq(article.user.userId)
                        .or(neighbor.neighborFrom.userId.eq(article.user.userId)))
                .where(
                        inTagTypeList(tagTypeList),
                        article.state.eq(1), // PLAIN
                        article.visibility.eq(1) // visibility가 1인 게시글만 조회
                )
                .fetch();

        List<Article> filteredArticleList = articleList.stream()
                .sorted((a1, a2) -> {
                    if (orderType == 1) {
                        // 좋아요 순서로 정렬
                        return Integer.compare(
                                articleLikeRepositorySupport.countLikesByArticleId(a2.getArticleId()),
                                articleLikeRepositorySupport.countLikesByArticleId(a1.getArticleId()));
                    } else {
                        // 최신순으로 정렬 (예: 생성 날짜 기준)
                        return a2.getCreatedAt().compareTo(a1.getCreatedAt());
                    }
                })
                .limit(5)
                .collect(Collectors.toList());

        return filteredArticleList;
    }
}
