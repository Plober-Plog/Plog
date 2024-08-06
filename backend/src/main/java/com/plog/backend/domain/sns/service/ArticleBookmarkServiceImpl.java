package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.sns.dto.request.ArticleBookmarkRequestDto;
import com.plog.backend.domain.sns.dto.response.ArticleBookmarkGetResponseDto;
import com.plog.backend.domain.sns.entity.Article;
import com.plog.backend.domain.sns.entity.ArticleBookmark;
import com.plog.backend.domain.sns.repository.ArticleBookmarkRepository;
import com.plog.backend.domain.sns.repository.ArticleBookmarkRepositorySupport;
import com.plog.backend.domain.sns.repository.ArticleRepository;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.global.auth.JwtTokenProvider;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service("articleBookmarkService")
public class ArticleBookmarkServiceImpl implements ArticleBookmarkService {

    public static JwtTokenUtil jwtTokenUtil;
    public static ArticleBookmarkRepository articleBookmarkRepository;
    public static UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleBookmarkRepositorySupport articleBookmarkRepositorySupport;

    //TODO [장현준] 비회원 안되는지 확인
    @Transactional
    @Override
    public void addBookmark(String token, ArticleBookmarkRequestDto articleBookmarkRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    return new NotValidRequestException("addArticleBookmark - 없는 사용자 입니다.");}
                );

        Long articleId = articleBookmarkRequestDto.getArticleId();
        Article article = articleRepository.findById(articleId).orElseThrow(()->{
            return new EntityNotFoundException("addBookmarkt - 없는 게시글 입니다.");
        });

        ArticleBookmark articleBookmark = ArticleBookmark.builder()
                .article(article)
                .user(user)
                .build();

        articleBookmarkRepository.save(articleBookmark);
    }

    @Transactional
    @Override
    public void deleteBookmark(String token, ArticleBookmarkRequestDto articleBookmarkRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    return new NotValidRequestException("deleteArticleBookmark - 없는 사용자 입니다.");}
                );

        Long articleId = articleBookmarkRequestDto.getArticleId();
        Article article = articleRepository.findById(articleId).orElseThrow(()->{
            return new EntityNotFoundException("deleteArticleBookmark - 없는 게시글 입니다.");
        });

        ArticleBookmark articleBookmark = articleBookmarkRepository.findById(articleId).orElseThrow(()->{
            return new EntityNotFoundException("deleteArticleBookmark - 없는 북마크입니다.");
        });

        articleBookmarkRepository.delete(articleBookmark);
    }

    @Override
    public ArticleBookmarkGetResponseDto getBookmarks(String token) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    return new NotValidRequestException("deleteArticleBookmark - 없는 사용자 입니다.");}
                );

        List<ArticleBookmark> articleBookmarks = articleBookmarkRepositorySupport.findByUser(user);

        List<ArticleBookmarkGetResponseDto.BookmarkDto> bookmarkDtos = articleBookmarks.stream()
                .map(bookmark -> ArticleBookmarkGetResponseDto.BookmarkDto.builder()
                        .id(bookmark.getArticleBookmarkId())
                        .articleId(bookmark.getArticle().getArticleId())
                        .articleContent(bookmark.getArticle().getContent())
                        .build())
                .collect(Collectors.toList());


        return ArticleBookmarkGetResponseDto.builder()
                .bookmarks(bookmarkDtos)
                .build();
    }
}
