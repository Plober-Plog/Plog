package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.sns.entity.ArticleBookmark;
import com.plog.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleBookmarkRepository extends JpaRepository<ArticleBookmark, Long> {
    List<ArticleBookmark> findByUser(User user);
}
