package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.sns.dto.request.ArticleCommentAddRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleCommentDeleteRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleCommentUpdateRequestDto;
import com.plog.backend.domain.sns.entity.ArticleComment;

public interface ArticleCommentService {
    void addArticleComment(ArticleCommentAddRequestDto articleCommentAddRequestDto);
    void updateArticleComment(ArticleCommentUpdateRequestDto articleCommentUpdateRequestDto);
    void deleteArticleComment(ArticleCommentDeleteRequestDto articleCommentDeleteRequestDto);
}
