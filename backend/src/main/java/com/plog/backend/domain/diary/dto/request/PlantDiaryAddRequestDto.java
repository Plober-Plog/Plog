package com.plog.backend.domain.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantDiaryAddRequestDto {
    @Schema(description = "식물 ID", example = "2")
    private Long plantId;

    @Schema(description = "SUNNY, CLOUDY, VERY_CLOUDY, RAINY 순서대로 1부터 시작", example = "1")
    private int weather;

    @Schema(description = "기온", example = "28.5")
    private float temperature;

    @Schema(description = "DRY, CLEAN,  NORMAL, MOIST, WET 순서대로 1부터 시작", example = "2")
    private int humidity;

    @Schema(description = "식물 일지 내용", example = "오늘은 식물이 잘 자랐어요.")
    private String content;

    @Schema(description = "일지 작성 일자", example = "2024-06-02")
    private LocalDate recordDate;

    @Schema(description = "대표 사진 index (0 ~ 4)", example = "2")
    private int thumbnailIdx;
}
