package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.sns.dto.request.ArticleCommentAddRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleCommentDeleteRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleCommentUpdateRequestDto;
import com.plog.backend.domain.sns.entity.ArticleComment;
import com.plog.backend.domain.sns.repository.ArticleCommentRepository;
import com.plog.backend.domain.sns.repository.ArticleRepository;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service("articleCommentService")
public class ArticleCommentServiceImpl implements ArticleCommentService {

    public static ArticleRepository articleRepository;
    public static ArticleCommentRepository articleCommentRepository;
    public static JwtTokenUtil jwtTokenUtil;
    public static UserRepository userRepository;

    @Override
    public void addArticleComment(String token, ArticleCommentAddRequestDto articleCommentAddRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                return new NotValidRequestException("없는 사용자 입니다.");}
                );

        Long articleId = articleCommentAddRequestDto.getArticleId();

        // 현재 article에 첫번쨰로 달린 Comment가 parentId, 혹은 아예 없다면 내가 parentId
        List<ArticleComment> Comments = articleRepository.findAllByArticleId(articleId);

        ArticleComment articleComment = ArticleComment.builder()
                .article(articleRepository.getReferenceById(articleId))
                .user(user)
                .parentId(!Comments.isEmpty() ? Comments.get(0).getParentId() : null)
                .content(articleCommentAddRequestDto.getContent())
                .state(1)
                //TODO [장현준] TagName
                .build();

        // 만약 ParentId가 없다면? 본인이 Parent
        if(articleComment.getParentId() == null)
            articleComment.setParentId(articleComment.getArticleCommentId());

        articleCommentRepository.save(articleComment);
    }

    @Transactional
    @Override
    public void updateArticleComment(String token, ArticleCommentUpdateRequestDto articleCommentUpdateRequestDto) {

    }

    @Override
    public void deleteArticleComment(String token, ArticleCommentDeleteRequestDto articleCommentDeleteRequestDto) {

    }
}
