package com.plog.backend.domain.diary.dto.response;

import com.plog.backend.domain.diary.entity.Humidity;
import com.plog.backend.domain.diary.entity.Weather;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantDiaryGetResponseDto {
    @Schema(description = "식물 일지 ID", example = "1")
    private Long plantDiaryId;

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

    @Schema(description = "일지에 등록된 사진 목록")
    private List<String> images;
}
