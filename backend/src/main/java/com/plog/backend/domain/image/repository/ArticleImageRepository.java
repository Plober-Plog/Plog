package com.plog.backend.domain.image.repository;

import com.plog.backend.domain.image.entity.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleImageRepository  extends JpaRepository<ArticleImage, Long> {
    List<ArticleImage> findByArticleArticleIdAndImageIsDeletedFalseOrderByOrderAsc(Long articleId);
}
