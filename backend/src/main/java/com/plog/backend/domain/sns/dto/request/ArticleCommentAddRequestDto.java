package com.plog.backend.domain.sns.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ArticleCommentAddRequestDto {
    private Long articleId;
    private String content;
    private Long parentId;
}
