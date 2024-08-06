package com.plog.backend.domain.plant.dto.response;

import com.plog.backend.domain.diary.dto.response.PlantDiaryGetResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantGetRecordsResponseDto {
    private Long plantId;

    private LocalDate date;

    private PlantCheckGetResponseDto plantCheck;

    private PlantDiaryGetResponseDto plantDiary;
}
