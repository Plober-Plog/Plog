package com.plog.backend.domain.plant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantCheckGetResponseDto {

    @Schema(description = "물 주기 여부", example = "true")
    boolean isWatered;

    @Schema(description = "영양제 주기 여부", example = "false")
    boolean isFertilized;

    @Schema(description = "분갈이 여부", example = "false")
    boolean isRepotted;

    @Schema(description = "점검 날짜", example = "2024-01-01")
    LocalDate checkDate;
}