package com.plog.backend.domain.plant.dto.response;

import com.plog.backend.domain.diary.dto.response.PlantDiaryGetResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantGetRecordsResponseDto {
    private PlantCheckGetResponseDto plantCheck;

    private PlantDiaryGetResponseDto plantDiary;
}
