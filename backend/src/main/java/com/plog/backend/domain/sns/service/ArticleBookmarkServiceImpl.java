package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.image.service.ImageService;
import com.plog.backend.domain.sns.dto.response.ArticleGetSimpleResponseDto;
import com.plog.backend.domain.sns.entity.Article;
import com.plog.backend.domain.sns.entity.ArticleBookmark;
import com.plog.backend.domain.sns.repository.*;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotValidRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.plog.backend.global.util.JwtTokenUtil.jwtTokenUtil;

@Slf4j
@RequiredArgsConstructor
@Service("articleBookmarkService")
public class ArticleBookmarkServiceImpl implements ArticleBookmarkService {

    @Value("${server.url}")
    private String serverUrl;

    private final ArticleBookmarkRepository articleBookmarkRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleBookmarkRepositorySupport articleBookmarkRepositorySupport;
    private final ImageService imageService;
    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeRepositorySupport articleLikeRepositorySupport;

    @Transactional
    @Override
    public void addBookmark(String token, Long articleId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> [addBookmark] - 사용자 ID: {}, 게시글 ID: {}", userId, articleId);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotValidRequestException("addArticleBookmark - 없는 사용자 입니다.")
                );

        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new EntityNotFoundException("addBookmark - 없는 게시글 입니다.")
        );

        Optional<ArticleBookmark> articleBookmarkOptional = articleBookmarkRepository.findByUserUserIdAndArticleArticleId(userId, articleId);

        if (articleBookmarkOptional.isPresent())
            throw new NotValidRequestException("이미 북마크한 게시글입니다.");

        ArticleBookmark articleBookmark = ArticleBookmark.builder()
                .article(article)
                .user(user)
                .build();

        articleBookmarkRepository.save(articleBookmark);
        log.info(">>> [addBookmark] - 북마크 등록 완료, 사용자 ID: {}, 게시글 ID: {}", userId, articleId);

        // 게시글 작성자에게 북마크 알림 보내기
        String sourceSearchId = user.getSearchId();
        String targetSearchId = article.getUser().getSearchId();
        String articleUrl = String.format("%s/sns/%d", serverUrl, articleId);
        if (!sourceSearchId.equals(targetSearchId)) {
            String type = "BOOKMARK";
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

    @Transactional
    @Override
    public void deleteBookmark(String token, Long articleId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> [deleteBookmark] - 사용자 ID: {}, 게시글 ID: {}", userId, articleId);

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotValidRequestException("deleteArticleBookmark - 없는 사용자 입니다.")
                );


        articleRepository.findById(articleId).orElseThrow(() ->
                new EntityNotFoundException("deleteArticleBookmark - 없는 게시글 입니다.")
        );

        ArticleBookmark articleBookmark = articleBookmarkRepository.findByUserUserIdAndArticleArticleId(userId, articleId).orElseThrow(() -> new EntityNotFoundException("deleteArticleBookmark - 없는 북마크입니다.")
        );

        articleBookmarkRepository.delete(articleBookmark);
        log.info(">>> [deleteBookmark] - 북마크 삭제 완료, 사용자 ID: {}, 게시글 ID: {}", userId, articleId);
    }

    @Override
    public List<ArticleGetSimpleResponseDto> getBookmarks(String token, int page) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> [getBookmarks] - 사용자 ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotValidRequestException("deleteArticleBookmark - 없는 사용자 입니다.")
                );

        List<Article> articleList = articleBookmarkRepositorySupport.loadBookmarkedArticleList(userId, page);
        List<ArticleGetSimpleResponseDto> articleGetResponseDtoList = new ArrayList<>();

        for (Article article : articleList) {
            List<String> articleImageList = imageService.loadImagUrlsByArticleId(article.getArticleId());
            int likeCnt = articleLikeRepository.countByArticleArticleId(article.getArticleId());
            boolean isLiked = articleLikeRepositorySupport.isLikedByUser(userId, article.getArticleId());
            int commentCnt = articleCommentRepository.countByArticleArticleIdAndState(article.getArticleId(), 1);
            articleGetResponseDtoList.add(ArticleGetSimpleResponseDto.builder()
                    .searchId(article.getUser().getSearchId())
                    .articleId(article.getArticleId())
                    .nickname(article.getUser().getNickname())
                    .profile(article.getUser().getImage().getImageUrl())
                    .createdAt(article.getCreatedAt().plusHours(9))
                    .content(article.getContent())
                    .view(article.getView())
                    .image(articleImageList.isEmpty() ? null : articleImageList.get(0))
                    .likeCnt(likeCnt)
                    .isLiked(isLiked)
                    .isBookmarked(true)
                    .commentCnt(commentCnt)
                    .build());
        }
        log.info(">>> [getBookmarks] - 북마크 목록 조회 완료, 사용자 ID: {}", userId);
        return articleGetResponseDtoList;
    }
}
