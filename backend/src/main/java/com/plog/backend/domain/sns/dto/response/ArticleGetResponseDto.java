package com.plog.backend.domain.sns.dto.response;

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

    @Schema(description = "사용자 ID", example = "1001")
    private Long userId;

    @Schema(description = "게시글 내용", example = "오늘은 날씨가 좋네요.")
    private String content;

    @Schema(description = "게시글 조회수", example = "150")
    private int view;

    @Schema(description = "게시글 공개 범위", example = "PUBLIC")
    private Visibility visibility;

    @Schema(description = "태그 타입 목록")
    private List<TagType> tagTypeList;
}
