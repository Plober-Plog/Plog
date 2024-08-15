package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.sns.entity.Article;
import com.plog.backend.domain.sns.entity.ArticleLike;
import com.plog.backend.domain.sns.repository.ArticleLikeRepository;
import com.plog.backend.domain.sns.repository.ArticleRepository;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service("articleLikeService")
public class ArticleLikeServiceImpl implements ArticleLikeService {

    @Value("${server.url}")
    private String serverUrl;

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Override
    public void addLikeToArticle(String token, Long articleId) {
        Long userId = JwtTokenUtil.jwtTokenUtil.getUserIdFromToken(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("일치하는 회원을 찾을 수 없습니다."));

        Article article = articleRepository.findById(articleId).orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        Optional<ArticleLike> articleLikeOptional = articleLikeRepository.findByUserUserIdAndArticleArticleId(userId, articleId);
        if (articleLikeOptional.isPresent())
            throw new NotValidRequestException("이미 좋아요한 게시글입니다.");
        ArticleLike articleLike = new ArticleLike();
        articleLike.setArticle(article);
        articleLike.setUser(userRepository.getReferenceById(userId));
        articleLikeRepository.save(articleLike);

        // 게시글 작성자에게 좋아요 알림 보내기
        String sourceSearchId = user.getSearchId();
        String targetSearchId = article.getUser().getSearchId();
        String articleUrl = String.format("%s/sns/%d", serverUrl, articleId);
        if (!sourceSearchId.equals(targetSearchId)) {
            String type = "LIKE";
            String urlString = String.format("%s/realtime/notification/send?sourceSearchId=%s&targetSearchId=%s&clickUrl=%s&type=%s",
                    serverUrl, sourceSearchId, targetSearchId, articleUrl, type);
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-type", "application/json");

                int responseCode = conn.getResponseCode();
                log.info("알림 전송 HTTP 응답 코드: " + responseCode);
                conn.disconnect();
            } catch (Exception e) {
                log.error("알림 전송 중 오류 발생", e);
            }
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
