package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.sns.entity.Article;
import com.plog.backend.domain.sns.entity.ArticleComment;
import com.plog.backend.domain.sns.entity.QArticleComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleCommentRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final int size = 15;

    public ArticleCommentRepositorySupport(JPAQueryFactory queryFactory) {
        super(ArticleComment.class);
        this.queryFactory = queryFactory;
    }

    public List<ArticleComment> findParentCommentsByArticleId(Article article) {
        QArticleComment articleComment = QArticleComment.articleComment;

        return queryFactory
                .selectFrom(articleComment)
                .where(articleComment.article.eq(article)
                        .and(articleComment.parentId.eq(articleComment.articleCommentId))) // articleCommentId = parentId -> 본인이 root
                .orderBy(articleComment.createdAt.asc())  // createdAt 기준 오름차순 정렬
                .fetch();
    }

    public List<ArticleComment> findChildCommentsByParentId(Long parentId) {
        QArticleComment articleComment = QArticleComment.articleComment;

        return queryFactory
                .selectFrom(articleComment)
                .where(articleComment.parentId.eq(parentId))
                .orderBy(articleComment.createdAt.asc())  // createdAt 기준 오름차순 정렬
                .fetch();
    }

    public List<ArticleComment> findCommentsByArticleId(Article article, int page) {
        QArticleComment articleComment = QArticleComment.articleComment;

        return queryFactory
                .selectFrom(articleComment)
                .where(articleComment.article.eq(article)) // articleCommentId = parentId -> 본인이 root
                .orderBy(articleComment.parentId.desc(), articleComment.articleCommentId.asc())  // parentId -> article_comment_id 순으로 정렬
                .offset(page * size)
                .limit(size)
                .fetch();
    }
}
