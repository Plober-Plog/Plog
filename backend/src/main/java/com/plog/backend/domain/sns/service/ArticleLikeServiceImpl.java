package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.sns.entity.Article;
import com.plog.backend.domain.sns.entity.ArticleLike;
import com.plog.backend.domain.sns.repository.ArticleLikeRepository;
import com.plog.backend.domain.sns.repository.ArticleRepository;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service("articleLikeService")
public class ArticleLikeServiceImpl implements ArticleLikeService {
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Override
    public void addLikeToArticle(String token, Long articleId) {
        Long userId = JwtTokenUtil.jwtTokenUtil.getUserIdFromToken(token);

        Optional<Article> article = articleRepository.findById(articleId);
        if (article.isPresent()) {
            Optional<ArticleLike> articleLikeOptional = articleLikeRepository.findByUserUserIdAndArticleArticleId(userId, articleId);
            if (articleLikeOptional.isPresent())
                throw new NotValidRequestException("이미 좋아요한 게시글입니다.");
            ArticleLike articleLike = new ArticleLike();
            articleLike.setArticle(article.get());
            articleLike.setUser(userRepository.getReferenceById(userId));
            articleLikeRepository.save(articleLike);
        } else {
            throw new EntityNotFoundException("좋아요를 등록할 게시글이 없습니다.");
        }
    }

    @Override
    public void removeLikeFromArticle(String token, Long articleId) {
        Long userId = JwtTokenUtil.jwtTokenUtil.getUserIdFromToken(token);
        Optional<ArticleLike> articleLike = articleLikeRepository.findByUserUserIdAndArticleArticleId(userId, articleId);
        if (articleLike.isPresent()) {
            articleLikeRepository.delete(articleLike.get());
        } else {
            throw new EntityNotFoundException("좋아요를 해제할 수 있는 상태가 아닙니다.");
        }
    }
}
