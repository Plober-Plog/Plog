package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.sns.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleTagRepositorySupport extends QuerydslRepositorySupport  {
    private final JPAQueryFactory queryFactory;

    public ArticleTagRepositorySupport(JPAQueryFactory queryFactory) {
        super(ArticleTag.class);
        this.queryFactory = queryFactory;
    }

    public List<TagType> findTagTypeByArticleId(Long articleId) {
        QArticleTag articleTag = QArticleTag.articleTag;
        QTagType tagType = QTagType.tagType;

        return queryFactory.select(Projections.constructor(
                        TagType.class,
                        tagType.tagTypeId,
                        tagType.tagName
                ))
                .from(articleTag)
                .join(articleTag.tagType, tagType)
                .where(articleTag.article.articleId.eq(articleId))
                .fetch();
    }

}
