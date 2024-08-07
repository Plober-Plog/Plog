package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.sns.dto.request.ArticleAddRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleUpdateRequestDto;
import com.plog.backend.domain.sns.dto.response.ArticleGetResponseDto;
import com.plog.backend.domain.sns.dto.response.ArticleGetSimpleResponseDto;
import com.plog.backend.domain.sns.entity.*;
import com.plog.backend.domain.sns.repository.*;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotAuthorizedRequestException;
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

    //TODO [강윤서] - 임시 이미지 url
    private final String tempImageUrl = "https://plogbucket.s3.ap-northeast-2.amazonaws.com/free-icon-sprout-267205.png";

    @Override
    public void uploadArticleImages(MultipartFile[] images, Long plantDiaryId) {

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
            log.info(articleTag.toString());
            articleTagList.add(articleTag);
        }
        articleTagRepository.saveAll(articleTagList);
        log.info(">>> addArticle - 게시글 태그 완료 : id {}", article.getArticleId());
        return article.getArticleId();
    }

    @Override
    public ArticleGetResponseDto getArticle(Long articleId) {
        //TODO [강윤서] - 게시글 조회 시 조회수 증가
        Optional<Article> article = articleRepository.findById(articleId);
        if (article.isPresent()) {
            List<TagType> tagTypeList = articleTagRepositorySupport.findTagTypeByArticleId(articleId);
            return ArticleGetResponseDto.builder()
                    .userId(article.get().getUser().getUserId())
                    .articleId(article.get().getArticleId())
                    .content(article.get().getContent())
                    .view(article.get().getView())
                    .tagTypeList(tagTypeList)
                    .visibility(article.get().getVisibility())
                    .build();
        } else {
            throw new EntityNotFoundException("게시글을 조회할 수 없습니다.");
        }
    }

    @Override
    public List<ArticleGetSimpleResponseDto> getArticleList(int page) {
        List<Article> articleList = articleRepositorySupport.loadArticleList(page);
        List<ArticleGetSimpleResponseDto> articleGetSimpleResponseDtoList = new ArrayList<>();
        for (Article article : articleList) {
            articleGetSimpleResponseDtoList.add(
                    ArticleGetSimpleResponseDto.builder()
                            .image(tempImageUrl)
                            .likeCnt(14)
                            .nickname("임시닉네임...")
                            .commentCnt(12)
                            .isBookmarked(false)
                            .content(article.getContent())
                            .build()
            );
        }
        //TODO [강윤서] - 게시글 목록 조회
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
            //TODO [강윤서] articleImageRepository에서 리스트 조회 후 삭제 -> 반복문으로 진행
            article.setState(State.DELETE);
            articleRepository.save(article);
            log.info(">>> deleteArticle - 게시글 삭제 완료: {}", article.getArticleId());
        } else {
            throw new EntityNotFoundException();
        }
    }
}
