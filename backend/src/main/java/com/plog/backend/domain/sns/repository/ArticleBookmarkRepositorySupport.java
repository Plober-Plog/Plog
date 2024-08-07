package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.sns.entity.*;
import com.plog.backend.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleBookmarkRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ArticleBookmarkRepositorySupport(JPAQueryFactory queryFactory) {
        super(ArticleComment.class);
        this.queryFactory = queryFactory;
    }

    public List<ArticleBookmark> findByUser(User user) {
        QArticleBookmark qArticleBookmark = QArticleBookmark.articleBookmark;

        return queryFactory
                .selectFrom(qArticleBookmark)
                .where(qArticleBookmark.user.eq(user))
                .orderBy(qArticleBookmark.createdAt.desc()) //TODO [장현준] - 최신순 부터?
                .fetch();
    }

    public boolean isBookmarkedByUser(Long userId, Long articleId) {
        QArticleBookmark qArticleBookmark = QArticleBookmark.articleBookmark;

        int count = (int) queryFactory
                .selectFrom(qArticleBookmark)
                .where(qArticleBookmark.user.userId.eq(userId)
                        .and(qArticleBookmark.article.articleId.eq(articleId)))
                .fetchCount();

        return count > 0;
    }
}
