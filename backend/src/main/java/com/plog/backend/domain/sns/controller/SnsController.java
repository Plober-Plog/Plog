package com.plog.backend.domain.sns.controller;

import com.plog.backend.domain.sns.dto.request.*;
import com.plog.backend.domain.sns.dto.response.ArticleBookmarkGetResponseDto;
import com.plog.backend.domain.sns.dto.response.ArticleCommentGetResponse;
import com.plog.backend.domain.sns.dto.response.ArticleGetResponseDto;
import com.plog.backend.domain.sns.dto.response.ArticleGetSimpleResponseDto;
import com.plog.backend.global.model.response.BaseResponseBody;
import com.plog.backend.domain.sns.service.ArticleBookmarkServiceImpl;
import com.plog.backend.domain.sns.service.ArticleCommentService;
import com.plog.backend.domain.sns.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "SNS API", description = "SNS 관련 API")
public class SnsController {
    private final ArticleService articleService;
    private final ArticleCommentService articleCommentService;
    private final ArticleBookmarkServiceImpl articleBookmarkService;

    // ============================= 게시글 =============================
    @PostMapping
    @Operation(summary = "게시글 등록", description = "새로운 게시글을 등록합니다.")
    public ResponseEntity<BaseResponseBody> addArticle(
            @RequestHeader("Authorization") String token,
            @ModelAttribute ArticleAddRequestDto articleAddRequestDto,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        log.info(">>> [POST] /user/sns - 요청 데이터: {} 이미지: {}", articleAddRequestDto, images == null ? "X" : "O");
        Long articleId = articleService.addArticle(token, articleAddRequestDto);
        if (images != null) {
            articleService.uploadArticleImages(images, articleId);
        }
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "게시글 등록이 완료되었습니다."));
    }

    @PatchMapping("{articleId}")
    @Operation(summary = "게시글 수정", description = "기존 게시글을 수정합니다.")
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
    @Operation(summary = "게시글 삭제", description = "기존 게시글을 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteArticle(
            @RequestHeader("Authorization") String token,
            @PathVariable Long articleId
    ) {
        log.info(">>> [DELETE] /user/sns/{} - 삭제 ID: {}", articleId, articleId);
        articleService.deleteArticle(token, articleId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "게시글 삭제가 완료되었습니다."));
    }

    @GetMapping("/{articleId}")
    @Operation(summary = "게시글 조회", description = "특정 게시글을 조회합니다.")
    public ResponseEntity<ArticleGetResponseDto> getPlantDiary(
            @PathVariable Long articleId) {
        log.info(">>> [GET] /user/sns/{} - 요청 ID: {}", articleId, articleId);
        ArticleGetResponseDto articleGetResponseDto = articleService.getArticle(articleId);
        return ResponseEntity.status(200).body(articleGetResponseDto);
    }

    @GetMapping
    @Operation(summary = "게시글 목록 조회", description = "모든 게시글을 페이지네이션하여 조회합니다.")
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
    @Operation(summary = "댓글 등록", description = "새로운 댓글을 등록합니다.")
    public ResponseEntity<BaseResponseBody> addComment(
            @RequestHeader("Authorization") String token,
            @RequestBody ArticleCommentAddRequestDto articleCommentAddRequestDto) {
        log.info(">>> [POST] /user/sns/comment - 댓글 생성 요청 데이터: {}", articleCommentAddRequestDto);
        articleCommentService.addArticleComment(token, articleCommentAddRequestDto);
        log.info(">>> [POST] /user/sns/comment - 댓글 생성 완료");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponseBody.of(200, "댓글이 생성되었습니다."));
    }

    @PatchMapping("/comment")
    @Operation(summary = "댓글 수정", description = "기존 댓글을 수정합니다.")
    public ResponseEntity<BaseResponseBody> updateComment(
            @RequestHeader("Authorization") String token,
            @RequestBody ArticleCommentUpdateRequestDto articleCommentUpdateRequestDto) {
        log.info(">>> [PATCH] /user/sns/comment - 댓글 수정 요청 데이터: {}", articleCommentUpdateRequestDto);
        articleCommentService.updateArticleComment(token, articleCommentUpdateRequestDto);
        log.info(">>> [PATCH] /user/sns/comment - 댓글 수정 완료");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponseBody.of(200, "댓글이 수정되었습니다."));
    }

    @DeleteMapping("/comment")
    @Operation(summary = "댓글 삭제", description = "기존 댓글을 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteComment(
            @RequestHeader("Authorization") String token,
            @RequestBody ArticleCommentDeleteRequestDto articleCommentDeleteRequestDto) {
        log.info(">>> [DELETE] /user/sns/comment - 댓글 삭제 요청 데이터: {}", articleCommentDeleteRequestDto);
        articleCommentService.deleteArticleComment(token, articleCommentDeleteRequestDto);
        log.info(">>> [DELETE] /user/sns/comment - 댓글 삭제 완료");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponseBody.of(200, "댓글이 삭제되었습니다."));
    }

//    @GetMapping("/{articleId}/comment")
//    @Operation(summary = "댓글 목록 조회", description = "특정 게시글의 모든 댓글을 조회합니다.")
//    public ResponseEntity<List<ArticleCommentGetResponse>> getComments(
//            @PathVariable("articleId") Long articleId) {
//        log.info(">>> [GET] /user/sns/{}/comment - 댓글 목록 조회 요청", articleId);
//        List<ArticleCommentGetResponse> comments = articleCommentService.getComments(articleId);
//        log.info(">>> [GET] /user/sns/{}/comment - 댓글 목록 조회 완료", articleId);
//        return ResponseEntity.status(HttpStatus.OK).body(comments);
//    }

    // ============================= 좋아요 =============================

    // ============================= 북마크 =============================
    @PostMapping("/bookmark/{articleId}")
    @Operation(summary = "북마크 추가", description = "특정 게시글을 북마크에 추가합니다.")
    public ResponseEntity<BaseResponseBody> addBookmark(
            @RequestHeader("Authorization") String token,
            @PathVariable("articleId") Long articleId) {
        log.info(">>> [POST] /user/sns/bookmark/{} - 북마크 추가 요청", articleId);
        articleBookmarkService.addBookmark(token, articleId);
        log.info(">>> [POST] /user/sns/bookmark/{} - 북마크 추가 완료", articleId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "북마크 등록이 되었습니다."));
    }

    @DeleteMapping("/bookmark/{articleId}")
    @Operation(summary = "북마크 삭제", description = "특정 게시글을 북마크에서 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteBookmark(
            @RequestHeader("Authorization") String token,
            @PathVariable("articleId") Long articleId) {
        log.info(">>> [DELETE] /user/sns/bookmark/{} - 북마크 삭제 요청", articleId);
        articleBookmarkService.deleteBookmark(token, articleId);
        log.info(">>> [DELETE] /user/sns/bookmark/{} - 북마크 삭제 완료", articleId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "북마크 삭제 되었습니다."));
    }

    @GetMapping("/bookmark")
    @Operation(summary = "북마크 목록 조회", description = "사용자의 모든 북마크를 조회합니다.")
    public ResponseEntity<ArticleBookmarkGetResponseDto> getBookmark(
            @RequestHeader("Authorization") String token) {
        log.info(">>> [GET] /user/sns/bookmark - 북마크 목록 조회 요청");
        ArticleBookmarkGetResponseDto response = articleBookmarkService.getBookmarks(token);
        log.info(">>> [GET] /user/sns/bookmark - 북마크 목록 조회 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
