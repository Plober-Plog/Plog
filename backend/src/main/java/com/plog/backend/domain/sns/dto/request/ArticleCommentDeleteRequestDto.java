package com.plog.backend.domain.sns.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ArticleCommentDeleteRequestDto {
    private Long commentId;
}
