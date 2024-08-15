package com.plog.backend.domain.sns.service;

public interface ArticleLikeService {
    void addLikeToArticle(String token, Long articleId);
    void removeLikeFromArticle(String token, Long articleId);
}
