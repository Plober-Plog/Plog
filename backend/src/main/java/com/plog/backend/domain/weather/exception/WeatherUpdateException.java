package com.plog.backend.domain.weather.exception;

import lombok.Getter;

@Getter
public class WeatherUpdateException extends RuntimeException {
    private final String message;

    public WeatherUpdateException() {
        this.message = "날씨 데이터를 업데이트하는 중 오류가 발생했습니다.";
    }

    public WeatherUpdateException(String message) {
        this.message = message;
    }
}
