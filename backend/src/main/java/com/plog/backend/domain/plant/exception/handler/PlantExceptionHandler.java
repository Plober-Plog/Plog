package com.plog.backend.domain.plant.exception.handler;

import com.plog.backend.domain.plant.exception.NotValidPlantTypeIdsException;
import com.plog.backend.domain.plant.exception.NotValidRequestException;
import com.plog.backend.global.exception.ExceptionResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PlantExceptionHandler {
    @ExceptionHandler(NotValidPlantTypeIdsException.class)
    public ResponseEntity<ExceptionResponseDto> handleNotValidPlantTypeIdsException(
            NotValidPlantTypeIdsException ex, HttpServletRequest request) {
        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotValidRequestException.class)
    public ResponseEntity<ExceptionResponseDto> handleNotValidRequestException(
            NotValidRequestException ex, HttpServletRequest request) {
        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
