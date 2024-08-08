package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.sns.entity.ArticleLike;
import com.plog.backend.domain.sns.entity.QArticleLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleLikeRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ArticleLikeRepositorySupport(JPAQueryFactory queryFactory) {
        super(ArticleLike.class);
        this.queryFactory = queryFactory;
    }

    public boolean isLikedByUser(Long userId, Long articleId) {
        QArticleLike qArticleLike = QArticleLike.articleLike;

        int count = (int) queryFactory
                .selectFrom(qArticleLike)
                .where(qArticleLike.user.userId.eq(userId)
                        .and(qArticleLike.article.articleId.eq(articleId)))
                .fetchCount();

        return count > 0;
    }
}
