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
    Long plantDiaryId;
    @Schema(description = "일지 작성 일자", example = "2024-06-02")
    LocalDate recordDate;
    //TODO [강윤서] - image 처리
}
