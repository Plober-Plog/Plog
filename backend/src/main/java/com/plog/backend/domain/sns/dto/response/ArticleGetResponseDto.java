package com.plog.backend.domain.sns.dto.response;

import com.plog.backend.domain.sns.entity.TagType;
import com.plog.backend.domain.sns.entity.Visibility;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ArticleGetResponseDto {
    private Long articleId;
    private Long userId;
    private String content;
    private int view;
    private Visibility visibility;
    private List<TagType> tagTypeList;
}
