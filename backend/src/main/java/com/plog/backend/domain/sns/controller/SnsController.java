package com.plog.backend.domain.sns.controller;

import com.plog.backend.domain.sns.dto.request.ArticleAddRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleUpdateRequestDto;
import com.plog.backend.domain.sns.dto.response.ArticleGetResponseDto;
import com.plog.backend.domain.sns.dto.response.ArticleGetSimpleResponseDto;
import com.plog.backend.domain.sns.service.ArticleCommentService;
import com.plog.backend.domain.sns.service.ArticleLikeServiceImpl;
import com.plog.backend.domain.sns.service.ArticleService;
import com.plog.backend.global.model.response.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/sns")
@Tag(name = "SNS Controller", description = "SNS API")
public class SnsController {
    private final ArticleService articleService;
    private final ArticleCommentService articleCommentService;
    private final ArticleLikeServiceImpl articleLikeService;

    // ============================= 게시글 =============================
    @PostMapping
    @Operation(summary = "게시글 추가", description = "새로운 게시글을 등록합니다.")
    public ResponseEntity<BaseResponseBody> addArticle(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader("Authorization") String token,
            @Parameter(description = "게시글 추가 요청 데이터", required = true) @ModelAttribute ArticleAddRequestDto articleAddRequestDto,
            @Parameter(description = "이미지 파일 목록") @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        log.info(">>> [POST] /user/sns - 요청 데이터: {} 이미지: {}",
                articleAddRequestDto, images == null ? "X" : "O");
        Long articleId = articleService.addArticle(token, articleAddRequestDto);
        if (images != null && images.length > 0) {
            articleService.uploadArticleImages(images, articleId);
        }
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "게시글 등록이 완료되었습니다."));
    }

    @PatchMapping("{articleId}")
    @Operation(summary = "게시글 수정", description = "게시글 ID로 게시글을 수정합니다.")
    public ResponseEntity<BaseResponseBody> updateArticle(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader("Authorization") String token,
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long articleId,
            @Parameter(description = "게시글 수정 요청 데이터", required = true) @RequestBody ArticleUpdateRequestDto articleUpdateRequestDto
    ) {
        log.info(">>> [PATCH] /user/sns/{} - 수정 ID: {}", articleId, articleId);
        articleUpdateRequestDto.setArticleId(articleId);
        articleService.updateArticle(token, articleUpdateRequestDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "게시글 수정이 완료되었습니다."));
    }

    @DeleteMapping("/{articleId}")
    @Operation(summary = "게시글 삭제", description = "게시글 ID로 게시글을 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteArticle(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader("Authorization") String token,
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long articleId
    ) {
        log.info(">>> [DELETE] /user/sns/{} - 삭제 ID: {}", articleId, articleId);
        articleService.deleteArticle(token, articleId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "게시글 삭제가 완료되었습니다."));
    }

    @GetMapping("/{articleId}")
    @Operation(summary = "게시글 조회", description = "게시글 ID로 게시글을 조회합니다.")
    public ResponseEntity<ArticleGetResponseDto> getArticle(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long articleId) {
        log.info(">>> [GET] /user/sns/{} - 요청 ID: {}", articleId, articleId);
        ArticleGetResponseDto articleGetResponseDto = articleService.getArticle(articleId);
        return ResponseEntity.status(200).body(articleGetResponseDto);
    }

    @GetMapping
    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 조회합니다.")
    public ResponseEntity<List<ArticleGetSimpleResponseDto>> getArticleList(
            @Parameter(description = "페이지 번호", required = false, example = "0") @RequestParam(required = false, defaultValue = "0") String page
    ) {
        log.info(">>> [GET] /user/sns?{} - 게시글 목록 ID: {}", page);
        List<ArticleGetSimpleResponseDto> articleGetSimpleResponseDtoList = articleService.getArticleList(Integer.parseInt(page));
        return ResponseEntity.status(200).body(articleGetSimpleResponseDtoList);
    }

    // ============================= 좋아요 =============================
    @PostMapping("/like")
    @Operation(summary = "게시글 좋아요 추가", description = "게시글에 좋아요를 추가합니다.")
    public ResponseEntity<BaseResponseBody> addLikeToArticle(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader("Authorization") String token,
            @Parameter(description = "게시글 ID", required = true) @RequestBody Long articleId
    ) {
        articleLikeService.addLikeToArticle(token, articleId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "좋아요 등록이 완료되었습니다."));
    }

    @DeleteMapping("/like")
    @Operation(summary = "게시글 좋아요 삭제", description = "게시글에 좋아요를 삭제합니다.")
    public ResponseEntity<BaseResponseBody> removeLikeFromArticle(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader("Authorization") String token,
            @Parameter(description = "게시글 ID", required = true) @RequestBody Long articleId
    ) {
        articleLikeService.removeLikeFromArticle(token, articleId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "좋아요 해제가 완료되었습니다."));
    }
    // ============================= 북마크 =============================
}
