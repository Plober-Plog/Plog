package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.sns.entity.ArticleBookmark;
import com.plog.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleBookmarkRepository extends JpaRepository<ArticleBookmark, Long> {
    Optional<ArticleBookmark> findByUserUserIdAndArticleArticleId(Long userId, Long articleId);
}
