package com.plog.backend.domain.diary.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantDiaryWeatherGetResponseDto {
    int weather;
    int humidity;
    double temperature;
}
