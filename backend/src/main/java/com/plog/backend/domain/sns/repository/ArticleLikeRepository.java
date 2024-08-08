package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.sns.entity.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    Optional<ArticleLike> findByUserUserIdAndArticleArticleId(Long userId, Long articleId);
    // 특정 게시글에 대한 좋아요 수를 조회하는 메서드
    int countByArticleArticleId(Long articleId);
}
