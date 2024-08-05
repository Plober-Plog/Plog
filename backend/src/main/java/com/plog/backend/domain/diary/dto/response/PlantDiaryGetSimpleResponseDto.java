package com.plog.backend.domain.diary.dto.response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlantDiaryGetSimpleResponseDto {
    @Schema(description = "식물 일지 ID", example = "2")
    private Long plantDiaryId;

    @Schema(description = "일지 작성 일자", example = "2024-06-02")
    private LocalDate recordDate;

    @Schema(description = "일지의 대표 사진")
    private String thumbnail;
}
