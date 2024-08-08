package com.plog.backend.domain.weather.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WeatherResponseDto {
    private double avgTempToday;
    private double avgHumidityToday;
    private int weatherToday;
    private int humidityToday;
    private double avgTempTomorrow;
    private double avgHumidityTomorrow;
    private int weatherTomorrow;
    private int humidityTomorrow;
}
