package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.image.service.ImageService;
import com.plog.backend.domain.sns.dto.response.ArticleBookmarkGetResponseDto;
import com.plog.backend.domain.sns.dto.response.ArticleGetResponseDto;
import com.plog.backend.domain.sns.entity.Article;
import com.plog.backend.domain.sns.entity.ArticleBookmark;
import com.plog.backend.domain.sns.entity.TagType;
import com.plog.backend.domain.sns.repository.*;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotValidRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.plog.backend.global.util.JwtTokenUtil.jwtTokenUtil;

@Slf4j
@RequiredArgsConstructor
@Service("articleBookmarkService")
public class ArticleBookmarkServiceImpl implements ArticleBookmarkService {

    private final ArticleBookmarkRepository articleBookmarkRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleBookmarkRepositorySupport articleBookmarkRepositorySupport;
    private final ImageService imageService;
    private final ArticleTagRepositorySupport articleTagRepositorySupport;
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

        Optional<ArticleBookmark> articleBookmarkOptional = articleBookmarkRepository.findById(articleId);

        if (articleBookmarkOptional.isPresent())
            throw new NotValidRequestException("이미 북마크한 게시글입니다.");

        ArticleBookmark articleBookmark = ArticleBookmark.builder()
                .article(article)
                .user(user)
                .build();

        articleBookmarkRepository.save(articleBookmark);
        log.info(">>> [addBookmark] - 북마크 등록 완료, 사용자 ID: {}, 게시글 ID: {}", userId, articleId);
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
    public List<ArticleGetResponseDto> getBookmarks(String token, int page) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> [getBookmarks] - 사용자 ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotValidRequestException("deleteArticleBookmark - 없는 사용자 입니다.")
                );

        List<Article> articleList = articleBookmarkRepositorySupport.loadBookmarkedArticleList(userId, page);
        List<ArticleGetResponseDto> articleGetResponseDtoList = new ArrayList<>();

        for (Article article : articleList) {
            List<TagType> tagTypeList = articleTagRepositorySupport.findTagTypeByArticleId(article.getArticleId());
            List<String> articleImageList = imageService.loadImagUrlsByArticleId(article.getArticleId());
            int likeCnt = articleLikeRepository.countByArticleArticleId(article.getArticleId());
            boolean isLiked = articleLikeRepositorySupport.isLikedByUser(userId, article.getArticleId());
            articleGetResponseDtoList.add(ArticleGetResponseDto.builder()
                    .searchId(article.getUser().getSearchId())
                    .articleId(article.getArticleId())
                    .content(article.getContent())
                    .view(article.getView())
                    .tagTypeList(tagTypeList)
                    .visibility(article.getVisibility())
                    .images(articleImageList)
                    .likeCnt(likeCnt)
                    .isLiked(isLiked)
                    .isBookmarked(true)
                    .build());
        }
        log.info(">>> [getBookmarks] - 북마크 목록 조회 완료, 사용자 ID: {}", userId);
        return articleGetResponseDtoList;
    }
}
