package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.sns.dto.request.ArticleBookmarkRequestDto;

public interface ArticleBookmarkService {
    void addBookmark(String token, ArticleBookmarkRequestDto articleBookmarkRequestDto);
    void deleteBookmark(String token, ArticleBookmarkRequestDto articleBookmarkRequestDto);
}
