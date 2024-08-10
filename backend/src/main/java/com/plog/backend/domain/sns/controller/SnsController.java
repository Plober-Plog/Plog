package com.plog.backend.domain.sns.controller;


import com.plog.backend.domain.sns.dto.request.*;
import com.plog.backend.domain.sns.dto.response.ArticleCommentGetResponse;
import com.plog.backend.domain.sns.dto.response.ArticleGetResponseDto;
import com.plog.backend.domain.sns.dto.response.ArticleGetSimpleResponseDto;
import com.plog.backend.domain.sns.entity.TagType;
import com.plog.backend.domain.sns.service.ArticleBookmarkServiceImpl;
import com.plog.backend.domain.sns.service.ArticleCommentService;
import com.plog.backend.domain.sns.service.ArticleLikeServiceImpl;
import com.plog.backend.domain.sns.service.ArticleService;
import com.plog.backend.global.exception.NoTokenRequestException;
import com.plog.backend.global.model.response.BaseResponseBody;
import com.plog.backend.global.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/sns")
@Tag(name = "SNS API", description = "SNS 관련 API")
public class SnsController {
    private final ArticleService articleService;
    private final ArticleCommentService articleCommentService;
    private final ArticleLikeServiceImpl articleLikeService;
    private final ArticleBookmarkServiceImpl articleBookmarkService;

    // ============================= 게시글 태그 =============================
    @GetMapping("/tag")
    public ResponseEntity<List<TagType>> getTagTypeList() {
        log.info(">>> [GET] /user/sns/tag : 게시글 태그 목록 조회");
        List<TagType> tagTypeList = articleService.getTagTypeList();
        return ResponseEntity.status(200).body(tagTypeList);
    }

    // ============================= 게시글 =============================
    @PostMapping
    @Operation(summary = "게시글 추가", description = "새로운 게시글을 등록합니다.")
    public ResponseEntity<BaseResponseBody> addArticle(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "게시글 추가 요청 데이터", required = true) @ModelAttribute ArticleAddRequestDto articleAddRequestDto,
            @Parameter(description = "이미지 파일 목록") @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        log.info(">>> [POST] /user/sns - 요청 데이터: {} 이미지 여부: {}",
                articleAddRequestDto, images == null ? "X" : "O");
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        Long articleId = articleService.addArticle(token, articleAddRequestDto);



        if (images != null && images.length > 0) {
            articleService.uploadArticleImages(images, articleId);
        }

        if(articleAddRequestDto.getTagTypeList().isEmpty()) {
            articleAddRequestDto.setTagTypeList(new ArrayList<>());
            articleAddRequestDto.getTagTypeList().add(0L);
        }


        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "게시글 등록이 완료되었습니다."));
    }

    @PatchMapping("/{articleId}")
    @Operation(summary = "게시글 수정", description = "게시글 ID로 게시글을 수정합니다.")
    public ResponseEntity<BaseResponseBody> updateArticle(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long articleId,
            @Parameter(description = "게시글 수정 요청 데이터", required = true) @RequestBody ArticleUpdateRequestDto articleUpdateRequestDto
    ) {
        log.info(">>> [PATCH] /user/sns/{} - 수정 ID: {}", articleId, articleId);
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        articleUpdateRequestDto.setArticleId(articleId);

        articleService.updateArticle(token, articleUpdateRequestDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "게시글 수정이 완료되었습니다."));
    }

    @DeleteMapping("/{articleId}")
    @Operation(summary = "게시글 삭제", description = "게시글 ID로 게시글을 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteArticle(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long articleId
    ) {
        log.info(">>> [DELETE] /user/sns/{} - 삭제 ID: {}", articleId, articleId);
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        articleService.deleteArticle(token, articleId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "게시글 삭제가 완료되었습니다."));
    }

    @GetMapping("/{articleId}")
    @Operation(summary = "게시글 조회", description = "게시글 ID로 게시글을 조회합니다.")
    public ResponseEntity<ArticleGetResponseDto> getArticle(
            @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long articleId
    ) {
        Long userId = 0L;
        if (token != null)
            userId = JwtTokenUtil.jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> [GET] /user/sns/{} - 요청 ID: {} | 현재 로그인한 회원의 id: {}", articleId, articleId, userId);
        ArticleGetResponseDto articleGetResponseDto = articleService.getArticle(userId, articleId);
        return ResponseEntity.status(200).body(articleGetResponseDto);
    }

    @GetMapping
    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 조회합니다.")
    public ResponseEntity<List<ArticleGetSimpleResponseDto>> getArticleList(
            @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "작성자 ID", example = "1")
            @RequestParam(value = "searchId", required = false) String searchId,
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "게시글 태그 필터링", example = "[1, 2]")
            @RequestParam(value = "tagType", required = false) List<Integer> tagType,
            @Parameter(description = "검색할 내용", example = "식물")
            @RequestParam(value = "keyword", required = false) String keyword,
            @Parameter(description = "검색할 범위(0: public, 1: 이웃, 2: 서로이웃)", example = "0")
            @RequestParam(value = "neighborType", required = false, defaultValue = "0") int neighborType,
            @Parameter(description = "정렬 방식(0: 최신순, 1: 좋아요순)", example = "1")
            @RequestParam(value = "orderType", required = false, defaultValue = "0") int orderType
    ) {
        Long userId = 0L;
        if (token != null)
            userId = JwtTokenUtil.jwtTokenUtil.getUserIdFromToken(token);

        log.info(">>> [GET] /user/sns?page={}&searchId={}&tagType={}&keyword={}&neighbor={}&orderType={} | 현재 로그인한 회원의 id: {}", page, searchId, tagType, keyword, neighborType, orderType, userId);

        ArticleGetListRequestDto articleGetListRequestDto = ArticleGetListRequestDto.builder()
                .userId(userId)
                .searchId(searchId)
                .tagType(tagType)
                .keyword(keyword)
                .neighborType(neighborType)
                .page(page)
                .orderType(orderType)
                .build();
        List<ArticleGetSimpleResponseDto> articleGetSimpleResponseDtoList = articleService.getArticleList(articleGetListRequestDto);
        return ResponseEntity.status(200).body(articleGetSimpleResponseDtoList);
    }

    @GetMapping("/top5")
    @Operation(summary = "게시글 목록 조회 Top5", description = "Top5 게시글 목록을 조회합니다.")
    public ResponseEntity<List<ArticleGetSimpleResponseDto>> getArticleTop5List(
            @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "게시글 태그 필터링", example = "[1, 2]")
            @RequestParam(value = "tagType", required = false) List<Integer> tagType,
            @Parameter(description = "정렬 방식(0: 최신순, 1: 좋아요순)", example = "1")
            @RequestParam(value = "orderType", required = false, defaultValue = "0") int orderType
    ) {
        Long userId = 0L;
        if (token != null)
            userId = JwtTokenUtil.jwtTokenUtil.getUserIdFromToken(token);

        log.info(">>> [GET] /user/sns?tagType={}&orderType={} | 현재 로그인한 회원의 id: {}", tagType, orderType,userId);

        ArticleGetTop5ListRequestDto articleGetTop5ListRequestDto = ArticleGetTop5ListRequestDto.builder()
                .userId(userId)
                .tagType(tagType)
                .orderType(orderType)
                .build();
        List<ArticleGetSimpleResponseDto> articleGetSimpleResponseDtoList = articleService.getArticleTop5List(articleGetTop5ListRequestDto);
        return ResponseEntity.status(200).body(articleGetSimpleResponseDtoList);
    }

    // ============================= 댓글 =============================
    @PostMapping("/comment")
    @Operation(summary = "댓글 등록", description = "새로운 댓글을 등록합니다.")
    public ResponseEntity<BaseResponseBody> addComment(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody ArticleCommentAddRequestDto articleCommentAddRequestDto) {
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
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
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody ArticleCommentUpdateRequestDto articleCommentUpdateRequestDto) {
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
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
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(value = "commentId") String commentId) {
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        log.info(">>> [DELETE] /user/sns/comment - 댓글 삭제 요청 데이터: {}", commentId);
        articleCommentService.deleteArticleComment(token, Long.parseLong(commentId));
        log.info(">>> [DELETE] /user/sns/comment - 댓글 삭제 완료");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponseBody.of(200, "댓글이 삭제되었습니다."));
    }

    @GetMapping("/{articleId}/comment")
    @Operation(summary = "댓글 목록 조회", description = "특정 게시글의 모든 댓글을 조회합니다.")
    public ResponseEntity<List<ArticleCommentGetResponse>> getComments(
            @PathVariable("articleId") Long articleId,
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(value = "page", required = false, defaultValue = "0") int page
    ) {
        log.info(">>> [GET] /user/sns/{}/comment - 댓글 목록 조회 요청", articleId);
        List<ArticleCommentGetResponse> comments = articleCommentService.getArticleComments(articleId, page);
        log.info(">>> [GET] /user/sns/{}/comment - 댓글 목록 조회 완료", articleId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }


    // ============================= 좋아요 =============================
    @PostMapping("/like/{articleId}")
    @Operation(summary = "게시글 좋아요 추가", description = "게시글에 좋아요를 추가합니다.")
    public ResponseEntity<BaseResponseBody> addLikeToArticle(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long articleId
    ) {
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        articleLikeService.addLikeToArticle(token, articleId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "좋아요 등록이 완료되었습니다."));
    }

    @DeleteMapping("/like/{articleId}")
    @Operation(summary = "게시글 좋아요 삭제", description = "게시글에 좋아요를 삭제합니다.")
    public ResponseEntity<BaseResponseBody> removeLikeFromArticle(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long articleId
    ) {
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        articleLikeService.removeLikeFromArticle(token, articleId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "좋아요 해제가 완료되었습니다."));
    }

    // ============================= 북마크 =============================
    @PostMapping("/bookmark/{articleId}")
    @Operation(summary = "북마크 추가", description = "특정 게시글을 북마크에 추가합니다.")
    public ResponseEntity<BaseResponseBody> addBookmark(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable("articleId") Long articleId) {
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        log.info(">>> [POST] /user/sns/bookmark/{} - 북마크 추가 요청", articleId);
        articleBookmarkService.addBookmark(token, articleId);
        log.info(">>> [POST] /user/sns/bookmark/{} - 북마크 추가 완료", articleId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "북마크 등록이 되었습니다."));
    }

    @DeleteMapping("/bookmark/{articleId}")
    @Operation(summary = "북마크 삭제", description = "특정 게시글을 북마크에서 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteBookmark(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable("articleId") Long articleId) {
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        log.info(">>> [DELETE] /user/sns/bookmark/{} - 북마크 삭제 요청", articleId);
        articleBookmarkService.deleteBookmark(token, articleId);
        log.info(">>> [DELETE] /user/sns/bookmark/{} - 북마크 삭제 완료", articleId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "북마크 삭제 되었습니다."));
    }

    @GetMapping("/bookmark")
    @Operation(summary = "북마크 목록 조회", description = "사용자의 모든 북마크를 조회합니다.")
    public ResponseEntity<List<ArticleGetResponseDto>> getBookmark(
            @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(value = "page", required = false, defaultValue = "0") int page) {
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        log.info(">>> [GET] /user/sns/bookmark - 북마크 목록 조회 요청");
        List<ArticleGetResponseDto> response = articleBookmarkService.getBookmarks(token, page);
        log.info(">>> [GET] /user/sns/bookmark - 북마크 목록 조회 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
