package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.neighbor.entity.QNeighbor;
import com.plog.backend.domain.sns.entity.Article;
import com.plog.backend.domain.sns.entity.QArticle;
import com.plog.backend.domain.sns.entity.QArticleTag;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ArticleRepositorySupport extends QuerydslRepositorySupport {
    private static final Logger logger = LoggerFactory.getLogger(ArticleRepositorySupport.class);

    private final JPAQueryFactory queryFactory;
    private final int size = 10;
    private final ArticleLikeRepositorySupport articleLikeRepositorySupport;

    @Autowired
    public ArticleRepositorySupport(JPAQueryFactory queryFactory, ArticleLikeRepositorySupport articleLikeRepositorySupport) {
        super(Article.class);
        this.queryFactory = queryFactory;
        this.articleLikeRepositorySupport = articleLikeRepositorySupport;
    }

    // TODO[장현준] : 나의 이웃 공개글이 내가 이웃을 추가했을 때 보이는 문제 발생
    public List<Article> loadArticleList(int page, String searchId, List<Integer> tagTypeList, String keyword, long userId, int neighborType, int orderType) {
        logger.info("Loading article list with page: {}, searchId: {}, userId: {}, neighborType: {}, orderType: {}", page, searchId, userId, neighborType, orderType);

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
                        article.state.eq(1), // PLAIN 상태인 게시글만 조회
                        filterByVisibility(userId, neighborType) // visibility 필터링 추가
                )
                .fetch();

        logger.debug("Fetched {} articles before filtering", articleList.size());

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

        logger.info("Returning {} filtered articles", filteredArticleList.size());

        return filteredArticleList;
    }

    private BooleanExpression eqSearchId(String searchId) {
        logger.debug("Filtering by searchId: {}", searchId);
        return searchId != null ? QArticle.article.user.searchId.eq(searchId) : null;
    }

    private BooleanExpression containsKeyword(String keyword) {
        logger.debug("Filtering by keyword: {}", keyword);
        return keyword != null && !keyword.isEmpty() ? QArticle.article.content.containsIgnoreCase(keyword) : null;
    }

    private BooleanExpression inTagTypeList(List<Integer> tagTypeList) {
        logger.debug("Filtering by tag types: {}", tagTypeList);
        return (tagTypeList != null && !tagTypeList.isEmpty()) ? QArticleTag.articleTag.tagType.tagTypeId.in(tagTypeList) : null;
    }

    private BooleanExpression filterByNeighborType(long userId, int neighborType) {
        logger.debug("Filtering by neighborType: {}", neighborType);
        QNeighbor neighbor = QNeighbor.neighbor;

        if (neighborType == 1) {
            return null; // 이웃 관계 없이 모든 게시글 조회
        } else if (neighborType == 2) {
            return neighbor.neighborFrom.userId.eq(userId).and(neighbor.neighborType.in(1))
                    .or(neighbor.neighborTo.userId.eq(userId).and(neighbor.neighborType.in(1)));
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

        // 기본적으로 visibility가 1인 경우는 항상 보이도록 설정
        BooleanExpression visibilityCondition = null;

        // visibility가 1,2인 경우 이웃 관계에 따라 필터링 추가
        if (neighborType == 1) {
            // neighborType이 1 일 때, visibility 1인 게시글 조회
            visibilityCondition = article.visibility.eq(1);
        } else if (neighborType == 2) {
            // neighborType이 2 일 때, visibility 2인 게시글을 이웃 관계에 따라 필터링
            BooleanExpression neighborCondition = neighbor.neighborFrom.user.userId.eq(userId)
                    .and(neighbor.neighborTo.user.userId.eq(article.user.userId))
                    .and(neighbor.neighborType.eq(1));
            visibilityCondition = article.visibility.eq(1)
                    .or(article.visibility.eq(2).and(neighborCondition));
        }

        // visibilityCondition이 null일 경우 기본값으로 true를 반환하여 필터링이 적용되지 않도록 처리
        return visibilityCondition != null ? visibilityCondition : article.isNotNull();
    }

    public List<Article> loadArticleTop5List(List<Integer> tagTypeList, int orderType) {
        logger.info("Loading top 5 articles with tag types: {}, orderType: {}", tagTypeList, orderType);

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

        logger.debug("Fetched {} articles before sorting", articleList.size());

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

        logger.info("Returning top 5 filtered articles");

        return filteredArticleList;
    }
}
