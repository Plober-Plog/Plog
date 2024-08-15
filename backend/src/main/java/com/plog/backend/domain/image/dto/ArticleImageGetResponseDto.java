package com.plog.backend.domain.image.dto;

import com.plog.backend.domain.image.entity.Image;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ArticleImageGetResponseDto {
    private Long articleImageId;

    private Long articleId;

    private Image image;

    private int order;
}
