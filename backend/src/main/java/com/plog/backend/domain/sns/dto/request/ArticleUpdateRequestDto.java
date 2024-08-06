package com.plog.backend.domain.sns.dto.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ArticleUpdateRequestDto {
    private Long articleId;

    private String content;

    private List<Long> tagTypeList;

    private int visibility;
}
