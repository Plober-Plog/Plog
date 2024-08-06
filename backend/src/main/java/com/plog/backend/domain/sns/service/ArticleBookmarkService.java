package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.sns.dto.request.ArticleBookmarkRequestDto;
import com.plog.backend.domain.sns.dto.response.ArticleBookmarkGetResponseDto;
import com.plog.backend.domain.sns.entity.ArticleBookmark;

import java.util.List;

public interface ArticleBookmarkService {
    void addBookmark(String token, ArticleBookmarkRequestDto articleBookmarkRequestDto);
    void deleteBookmark(String token, ArticleBookmarkRequestDto articleBookmarkRequestDto);
    ArticleBookmarkGetResponseDto getBookmarks(String token);
}
