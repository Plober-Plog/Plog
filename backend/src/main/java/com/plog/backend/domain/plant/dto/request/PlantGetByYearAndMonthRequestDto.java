package com.plog.backend.domain.plant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantGetByYearAndMonthRequestDto {
    @Schema(description = "식물 ID", example = "1")
    private Long plantId;

    @Schema(description = "조회를 원하는 연", example = "2024")
    int year;

    @Schema(description = "조회를 원하는 월", example = "11")
    int month;
}
