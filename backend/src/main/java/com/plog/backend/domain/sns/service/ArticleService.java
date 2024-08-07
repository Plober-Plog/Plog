package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.plant.dto.request.PlantGetRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponseDto;
import com.plog.backend.domain.sns.dto.request.ArticleAddRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleGetListRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleUpdateRequestDto;
import com.plog.backend.domain.sns.dto.response.ArticleGetResponseDto;
import com.plog.backend.domain.sns.dto.response.ArticleGetSimpleResponseDto;
import com.plog.backend.domain.sns.entity.TagType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {
    List<TagType> getTagTypeList();

    void uploadArticleImages(MultipartFile[] images, Long articleId);

    Long addArticle(String token, ArticleAddRequestDto articleAddRequestDto);

    ArticleGetResponseDto getArticle(Long userId, Long articleId);

    List<ArticleGetSimpleResponseDto> getArticleList(ArticleGetListRequestDto articleGetListRequestDto);

    void updateArticle(String token, ArticleUpdateRequestDto articleUpdateRequestDto);

    void deleteArticle(String token, Long articleId);

    void updateView(Long articleId);

//    void reportArticle(String token, Long articleId);
}
