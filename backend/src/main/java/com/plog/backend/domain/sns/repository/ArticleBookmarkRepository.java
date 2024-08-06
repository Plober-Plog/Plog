package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.sns.entity.ArticleBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleBookmarkRepository extends JpaRepository<ArticleBookmark, Long> {
}
