package com.plog.backend.domain.plant.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantCheckAddRequestDto {
    @Schema(description = "식물 ID", example = "1")
    private Long plantId;

    @Schema(description = "물 주기 여부", example = "true")
    @JsonProperty("isWatered")
    private boolean isWatered;

    @Schema(description = "영양제 주기 여부", example = "false")
    @JsonProperty("isFertilized")
    private boolean isFertilized;

    @Schema(description = "분갈이 여부", example = "false")
    @JsonProperty("isRepotted")
    private boolean isRepotted;

    @Schema(description = "점검 날짜", example = "2024-01-01")
    private LocalDate checkDate;
}
