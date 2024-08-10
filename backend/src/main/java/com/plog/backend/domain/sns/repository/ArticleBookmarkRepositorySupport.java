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
    private final int size = 15;

    public ArticleBookmarkRepositorySupport(JPAQueryFactory queryFactory) {
        super(ArticleComment.class);
        this.queryFactory = queryFactory;
    }

    public List<ArticleBookmark> findByUser(User user, int page) {
        QArticleBookmark qArticleBookmark = QArticleBookmark.articleBookmark;

        return queryFactory
                .selectFrom(qArticleBookmark)
                .where(qArticleBookmark.user.eq(user))
                .orderBy(qArticleBookmark.createdAt.desc()) //TODO [장현준] - 최신순 부터?
                .offset(page * size)
                .limit(size)
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

    public List<Article> loadBookmarkedArticleList(Long userId, int page) {
        QArticle article = QArticle.article;
        QArticleBookmark articleBookmark = QArticleBookmark.articleBookmark;

        return queryFactory.selectFrom(article)
                .join(articleBookmark).on(articleBookmark.article.articleId.eq(article.articleId))
                .where(articleBookmark.user.userId.eq(userId))
                .orderBy(article.createdAt.desc())  // 최신순으로 정렬
                .offset(page * size)
                .limit(size)
                .fetch();
    }
}
