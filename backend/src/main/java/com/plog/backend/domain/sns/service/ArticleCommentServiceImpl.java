package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.sns.dto.request.ArticleCommentAddRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleCommentDeleteRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleCommentUpdateRequestDto;
import org.springframework.stereotype.Service;

@Service("articleCommentService")
public class ArticleCommentServiceImpl implements ArticleCommentService {

    @Override
    public void addArticleComment(ArticleCommentAddRequestDto articleCommentAddRequestDto) {
        
    }

    @Override
    public void updateArticleComment(ArticleCommentUpdateRequestDto articleCommentUpdateRequestDto) {

    }

    @Override
    public void deleteArticleComment(ArticleCommentDeleteRequestDto articleCommentDeleteRequestDto) {

    }
}
