package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.image.entity.ArticleImage;
import com.plog.backend.domain.image.repository.ArticleImageRepository;
import com.plog.backend.domain.image.service.ImageServiceImpl;
import com.plog.backend.domain.sns.dto.request.ArticleAddRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleGetListRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleUpdateRequestDto;
import com.plog.backend.domain.sns.dto.response.ArticleGetResponseDto;
import com.plog.backend.domain.sns.dto.response.ArticleGetSimpleResponseDto;
import com.plog.backend.domain.sns.entity.*;
import com.plog.backend.domain.sns.repository.*;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotAuthorizedRequestException;
import com.plog.backend.global.exception.NotValidRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.plog.backend.global.util.JwtTokenUtil.jwtTokenUtil;

@Slf4j
@RequiredArgsConstructor
@Service("articleService")
public class ArticleServiceImpl implements ArticleService {
    private final UserRepository userRepository;
    private final ArticleTagRepository articleTagRepository;
    private final ArticleRepository articleRepository;
    private final ArticleTagRepositorySupport articleTagRepositorySupport;
    private final TagTypeRepository tagTypeRepository;
    private final ArticleRepositorySupport articleRepositorySupport;
    private final ArticleImageRepository articleImageRepository;
    private final ArticleBookmarkRepositorySupport articleBookmarkRepositorySupport;

    //TODO [강윤서] - 임시 이미지 url
    private final String tempImageUrl = "https://plogbucket.s3.ap-northeast-2.amazonaws.com/free-icon-sprout-267205.png";
    private final ArticleLikeRepository articleLikeRepository;
    private final ImageServiceImpl imageService;
    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleBookmarkRepository articleBookmarkRepository;
    private final ArticleLikeRepositorySupport articleLikeRepositorySupport;

    @Override
    public List<TagType> getTagTypeList() {
        return tagTypeRepository.findAll();
    }

    @Override
    public void uploadArticleImages(MultipartFile[] images, Long articleId) {
        if (images.length > 10)
            throw new NotValidRequestException("게시글에는 최대 10개 사진을 업로드 할 수 있습니다.");
        imageService.ArticleUploadImages(images, articleId);
    }

    @Override
    public Long addArticle(String token, ArticleAddRequestDto articleAddRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> addArticle - 요청 데이터: {}", articleAddRequestDto);
        Article article = articleRepository.save(
                Article.builder()
                        .user(userRepository.getReferenceById(userId))
                        .content(articleAddRequestDto.getContent())
                        .visibility(articleAddRequestDto.getVisibility())
                        .state(1)
                        .build());
        log.info(">>> addArticle - 게시글 내용 저장 완료 : id {}", article.getArticleId());
        List<ArticleTag> articleTagList = new ArrayList<>();
        for (Long tagTypeId : articleAddRequestDto.getTagTypeList()) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticle(article);
            TagType tagType = tagTypeRepository.findById(tagTypeId)
                    .orElseThrow(() -> new EntityNotFoundException("TagType not found with id " + tagTypeId));
            articleTag.setTagType(tagType);
            articleTagList.add(articleTag);
        }
        articleTagRepository.saveAll(articleTagList);
        log.info(">>> addArticle - 게시글 태그 완료 : id {}", article.getArticleId());
        return article.getArticleId();
    }


    @Override
    public ArticleGetResponseDto getArticle(Long userId, Long articleId) {
        Optional<Article> article = articleRepository.findById(articleId);
        if (article.isPresent()) {
            updateView(articleId); // 조회수 증가
            List<TagType> tagTypeList = articleTagRepositorySupport.findTagTypeByArticleId(articleId);
            List<String> articleImageList = imageService.loadImagUrlsByArticleId(articleId);
            log.info(">>> getArticle - 게시글 조회 완료 : id {}", articleId);
            int likeCnt = articleLikeRepository.countByArticleArticleId(articleId);
            boolean isLiked = articleLikeRepositorySupport.isLikedByUser(userId, articleId);
            boolean isBookmarked = articleBookmarkRepositorySupport.isBookmarkedByUser(userId, articleId);
            return ArticleGetResponseDto.builder()
                    .searchId(article.get().getUser().getSearchId())
                    .articleId(article.get().getArticleId())
                    .content(article.get().getContent())
                    .view(article.get().getView())
                    .tagTypeList(tagTypeList)
                    .visibility(article.get().getVisibility())
                    .images(articleImageList)
                    .likeCnt(likeCnt)
                    .isLiked(isLiked)
                    .isBookmarked(isBookmarked)
                    .build();
        } else {
            throw new EntityNotFoundException("게시글을 조회할 수 없습니다.");
        }
    }

    @Override
    public List<ArticleGetSimpleResponseDto> getArticleList(ArticleGetListRequestDto articleGetListRequestDto) {
        int page = articleGetListRequestDto.getPage();
        String searchId = articleGetListRequestDto.getSearchId();
        List<Integer> tagTypeList = articleGetListRequestDto.getTagType();
        String keyword = articleGetListRequestDto.getKeyword();

        List<Article> articleList = articleRepositorySupport.loadArticleList(page, searchId, tagTypeList, keyword);
        List<ArticleGetSimpleResponseDto> articleGetSimpleResponseDtoList = new ArrayList<>();
        for (Article article : articleList) {
            List<String> articleImageList = imageService.loadImagUrlsByArticleId(article.getArticleId());
            int likeCnt = articleLikeRepository.countByArticleArticleId(article.getArticleId());
            int commentCnt = articleCommentRepository.countByArticleArticleId(article.getArticleId());
            boolean isBookmarked = articleBookmarkRepositorySupport.isBookmarkedByUser(articleGetListRequestDto.getUserId(), article.getArticleId());
            articleGetSimpleResponseDtoList.add(
                    ArticleGetSimpleResponseDto.builder()
                            .image(articleImageList.size() == 0 ? null : articleImageList.get(0)) // 첫 번째 사진의 url 전달
                            .likeCnt(likeCnt)
                            .view(article.getView())
                            .nickname(article.getUser().getNickname())
                            .commentCnt(commentCnt)
                            .isBookmarked(isBookmarked)
                            .content(article.getContent())
                            .build()
            );
        }
        return articleGetSimpleResponseDtoList;
    }

    @Transactional
    @Override
    public void updateArticle(String token, ArticleUpdateRequestDto articleUpdateRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        Optional<Article> articleOptional = articleRepository.findById(articleUpdateRequestDto.getArticleId());
        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            if (userId != article.getUser().getUserId())
                throw new NotAuthorizedRequestException();
            article.setContent(articleUpdateRequestDto.getContent());
            article.setVisibility(Visibility.visibility(articleUpdateRequestDto.getVisibility()));

            // article에 등록된 태그 해제
            articleTagRepository.deleteAllByArticleArticleId(article.getArticleId());

            log.info(">>> updateArticle - 게시글 기존 태그 삭제 완료 : id {}", article.getArticleId());
            // 다시 태그 달기
            List<ArticleTag> articleTagList = new ArrayList<>();
            for (Long tagTypeId : articleUpdateRequestDto.getTagTypeList()) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticle(article);
                TagType tagType = tagTypeRepository.findById(tagTypeId)
                        .orElseThrow(() -> new EntityNotFoundException("TagType not found with id " + tagTypeId));
                articleTag.setTagType(tagType);
                articleTagList.add(articleTag);
            }
            articleTagRepository.saveAll(articleTagList);

            log.info(">>> updateArticle - 게시글 수정 완료 : id {}", article.getArticleId());

        } else {
            throw new EntityNotFoundException("게시글을 조회할 수 없습니다.");
        }

    }

    @Transactional
    @Override
    public void deleteArticle(String token, Long articleId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            if (userId != article.getUser().getUserId())
                throw new NotAuthorizedRequestException();
            List<ArticleImage> articleImageList = articleImageRepository.findByArticleArticleIdAndImageIsDeletedFalseOrderByOrderAsc(articleId);
            for (ArticleImage articleImage : articleImageList) {
                imageService.deleteImage(articleImage.getImage().getImageUrl());
            }
            article.setState(State.DELETE);
            articleRepository.save(article);
            log.info(">>> deleteArticle - 게시글 삭제 완료: {}", article.getArticleId());
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public void updateView(Long articleId) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        if (articleOptional.isPresent()) {
            int view = articleOptional.get().getView();
            articleOptional.get().setView(view+1);
            articleRepository.save(articleOptional.get());
            log.info(">>> updateView - 게시글 조회수 +1 증가: {}");
        } else {
            throw new EntityNotFoundException("일치하는 게시글을 조회할 수 없습니다.");
        }
    }
}
