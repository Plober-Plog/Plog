package com.plog.backend.domain.sns.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ArticleBookmarkRequestDto {
    private Long articleId;
}
