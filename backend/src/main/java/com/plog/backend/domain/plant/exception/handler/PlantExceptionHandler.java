package com.plog.backend.domain.plant.exception.handler;

import com.plog.backend.domain.plant.exception.NotValidPlantTypeIdsException;
import com.plog.backend.global.exception.ExceptionResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PlantExceptionHandler {
    @ExceptionHandler(NotValidPlantTypeIdsException.class)
    public ResponseEntity<ExceptionResponseDto> handleConflictingPlantTypeIdsException(
            NotValidPlantTypeIdsException ex, HttpServletRequest request) {

        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                "유효한 식물 종류가 아닙니다."
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
