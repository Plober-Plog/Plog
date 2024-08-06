package com.plog.backend.domain.sns.controller;

import com.plog.backend.domain.sns.dto.request.*;
import com.plog.backend.domain.sns.dto.response.ArticleCommentGetResponse;
import com.plog.backend.domain.sns.dto.response.ArticleGetResponseDto;
import com.plog.backend.domain.sns.dto.response.ArticleGetSimpleResponseDto;
import com.plog.backend.domain.sns.service.ArticleCommentService;
import com.plog.backend.domain.sns.service.ArticleService;
import com.plog.backend.global.model.response.BaseResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/sns")
public class SnsController {
    private final ArticleService articleService;
    private final ArticleCommentService articleCommentService;

    // ============================= 게시글 =============================
    @PostMapping
    public ResponseEntity<BaseResponseBody> addArticle(
            @RequestHeader("Authorization") String token,
            @ModelAttribute ArticleAddRequestDto articleAddRequestDto,
            @RequestPart(value = "images", required = false) MultipartFile[] images
            ) {
        log.info(">>> [POST] /user/sns - 요청 데이터: {} 이미지: {}",
                articleAddRequestDto, images == null ? "X" : "O");
        Long articleId = articleService.addArticle(token, articleAddRequestDto);
        // 요청으로 넘어온 이미지 리스트가 있으면 호출
        if (images != null) {
            articleService.uploadArticleImages(images, articleId);
        }
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "게시글 등록이 완료되었습니다."));
    }

    @PatchMapping("{articleId}")
    public ResponseEntity<BaseResponseBody> updateArticle(
            @RequestHeader("Authorization") String token,
            @PathVariable Long articleId,
            @RequestBody ArticleUpdateRequestDto articleUpdateRequestDto
    ) {
        log.info(">>> [PATCH] /user/sns/{} - 수정 ID: {}", articleId, articleId);
        articleUpdateRequestDto.setArticleId(articleId);
        articleService.updateArticle(token, articleUpdateRequestDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "게시글 수정이 완료되었습니다."));
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<BaseResponseBody> deleteArticle(
            @RequestHeader("Authorization") String token,
            @PathVariable Long articleId
    ) {
        log.info(">>> [DELETE] /user/sns/{} - 삭제 ID: {}", articleId, articleId);
        articleService.deleteArticle(token, articleId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "게시글 삭제가 완료되었습니다."));
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleGetResponseDto> getPlantDiary(
            @PathVariable Long articleId) {
        log.info(">>> [GET] /user/sns/{} - 요청 ID: {}", articleId, articleId);
        ArticleGetResponseDto articleGetResponseDto = articleService.getArticle(articleId);
        return ResponseEntity.status(200).body(articleGetResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<ArticleGetSimpleResponseDto>> getArticleList(
            @RequestParam(required = false, defaultValue = "0") String page
    ) {
        log.info(">>> [GET] /user/sns?{} - 게시글 목록 ID: {}", page);
        List<ArticleGetSimpleResponseDto> articleGetSimpleResponseDtoList =
                articleService.getArticleList(Integer.parseInt(page));
        return ResponseEntity.status(200).body(articleGetSimpleResponseDtoList);
    }

    // ============================= 댓글 =============================
    @PostMapping("/comment")
    public ResponseEntity<BaseResponseBody> addComment(
            @RequestHeader("Authorization") String token,
            @RequestBody ArticleCommentAddRequestDto articleCommentAddRequestDto) {
        articleCommentService.addArticleComment(token, articleCommentAddRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponseBody.of(200,"댓글이 생성되었습니다."));
    }

    @PatchMapping("/comment")
    public ResponseEntity<BaseResponseBody> updateComment(
            @RequestHeader("Authorization") String token,
            @RequestBody ArticleCommentUpdateRequestDto articleCommentUpdateRequestDto) {
        articleCommentService.updateArticleComment(token, articleCommentUpdateRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponseBody.of(200,"댓글이 수정되었습니다."));
    }

    @DeleteMapping("/comment")
    public ResponseEntity<BaseResponseBody> deleteComment(
            @RequestHeader("Authorization") String token,
            @RequestBody ArticleCommentDeleteRequestDto articleCommentDeleteRequestDto) {
        articleCommentService.deleteArticleComment(token, articleCommentDeleteRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponseBody.of(200,"댓글이 수정되었습니다."));
    }

    @GetMapping("/{articleId}/comment")
    public ResponseEntity<List<ArticleCommentGetResponse>> getComments(
            @PathVariable("articleId") Long articleId) {
            return null;
    }


    // ============================= 좋아요 =============================

    // ============================= 북마크 =============================
    @PostMapping("/bookmark/{")
    public ResponseEntity<BaseResponseBody> addBookmark(@PathVariable("articleId") Long articleId) {
        return null;
    }

    @DeleteMapping("/bookmark/{}")
    public ResponseEntity<BaseResponseBody> deleteBookmark(@PathVariable("articleId") Long articleId) {
        return null;
    }
}
