package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.sns.entity.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
    void deleteAllByArticleArticleId(Long articleId);
}
