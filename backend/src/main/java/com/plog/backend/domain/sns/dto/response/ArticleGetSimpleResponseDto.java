package com.plog.backend.domain.sns.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ArticleGetSimpleResponseDto {
    @Schema(description = "게시글 ID", example = "1")
    private Long articleId;
    String nickname;
    String image;
    String content;
    int likeCnt;
    int commentCnt;
    int view;
    @JsonProperty("isLiked")
    boolean isLiked;
    @JsonProperty("isBookmarked")
    boolean isBookmarked;
}
