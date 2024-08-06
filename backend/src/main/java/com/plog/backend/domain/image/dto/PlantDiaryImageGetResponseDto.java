package com.plog.backend.domain.image.dto;

import com.plog.backend.domain.image.entity.Image;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlantDiaryImageGetResponseDto {

    private Long plantDiaryImageId;


    private Long plantDiaryId;


    private Image image;


    private int order;

    private boolean isThumbnail = false;
}
