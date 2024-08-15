package com.plog.backend.domain.sns.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ArticleGetSimpleResponseDto {
    @Schema(description = "게시글 ID", example = "1")
    private Long articleId;
    private String nickname;
    private String searchId;
    private String profile;
    private LocalDateTime createdAt;
    private String image;
    private String content;
    private int likeCnt;
    private int commentCnt;
    private int view;
    @JsonProperty("isLiked")
    private boolean isLiked;
    @JsonProperty("isBookmarked")
    private boolean isBookmarked;
}
