package com.plog.backend.domain.sns.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.plog.backend.domain.sns.entity.TagType;
import com.plog.backend.domain.sns.entity.Visibility;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Schema(description = "게시글 상세 응답 DTO")
public class ArticleGetResponseDto {

    @Schema(description = "게시글 ID", example = "1")
    private Long articleId;

    @Schema(description = "사용자 searchId", example = "test")
    private String searchId;

    @Schema(description = "게시글 내용", example = "오늘은 날씨가 좋네요.")
    private String content;

    @Schema(description = "게시글 조회수", example = "150")
    private int view;

    @Schema(description = "게시글에 달린 좋아요 수", example = "101")
    private int likeCnt;

    @Schema(description = "로그인한 회원이 해당 게시글에 좋아요를 눌렀는가", example = "false")
    @JsonProperty("isLiked")
    private boolean isLiked;

    @Schema(description = "로그인한 회원이 해당 게시글에 북마크를 눌렀는가", example = "false")
    @JsonProperty("isBookmarked")
    private boolean isBookmarked;

    @Schema(description = "등록된 게시글 사진 리스트", example = "test")
    private List<String> images;

    @Schema(description = "게시글 공개 범위", example = "PUBLIC")
    private Visibility visibility;

    @Schema(description = "태그 타입 목록")
    private List<TagType> tagTypeList;
}
