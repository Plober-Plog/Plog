package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.sns.dto.request.ArticleCommentAddRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleCommentDeleteRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleCommentUpdateRequestDto;
import com.plog.backend.domain.sns.entity.ArticleComment;

import java.util.List;

public interface ArticleCommentService {
    void addArticleComment(String token, ArticleCommentAddRequestDto articleCommentAddRequestDto);
    void updateArticleComment(String token, ArticleCommentUpdateRequestDto articleCommentUpdateRequestDto);
    void deleteArticleComment(String token, ArticleCommentDeleteRequestDto articleCommentDeleteRequestDto);
    List<List<ArticleComment>> getArticleComments(Long articleId);
}
