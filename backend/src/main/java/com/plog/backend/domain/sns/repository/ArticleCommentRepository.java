package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.sns.entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}
