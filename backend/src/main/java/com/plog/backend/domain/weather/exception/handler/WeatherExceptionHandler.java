package com.plog.backend.domain.weather.exception.handler;

import com.plog.backend.domain.weather.exception.WeatherUpdateException;
import com.plog.backend.global.exception.model.ExceptionResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@Component
public class WeatherExceptionHandler {

    @EventListener
    public void handleWeatherUpdateException(WeatherUpdateException ex) {
        log.error("스케줄링된 작업에서 WeatherUpdateException 발생 - Message: {}", ex.getMessage());
    }

    @ExceptionHandler(WeatherUpdateException.class)
    public ResponseEntity<ExceptionResponseDto> handleWeatherUpdateException(
            WeatherUpdateException ex, HttpServletRequest request) {
        log.error("WeatherUpdateException 발생 - URL: {}, Message: {}", request.getRequestURI(), ex.getMessage());
        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
