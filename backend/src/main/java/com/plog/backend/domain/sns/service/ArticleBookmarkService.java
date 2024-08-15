package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.sns.dto.response.ArticleGetSimpleResponseDto;

import java.util.List;

public interface ArticleBookmarkService {
    void addBookmark(String token, Long articleId);
    void deleteBookmark(String token, Long articleId);
    List<ArticleGetSimpleResponseDto> getBookmarks(String token, int page);
}
