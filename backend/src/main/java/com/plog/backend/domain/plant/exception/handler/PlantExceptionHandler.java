package com.plog.backend.domain.plant.exception.handler;

import com.plog.backend.domain.plant.exception.NotValidPlantTypeIdsException;
import com.plog.backend.global.exception.model.ExceptionResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class PlantExceptionHandler {
    @ExceptionHandler(NotValidPlantTypeIdsException.class)
    public ResponseEntity<ExceptionResponseDto> handleNotValidPlantTypeIdsException(
            NotValidPlantTypeIdsException ex, HttpServletRequest request) {
        log.error("NotValidPlantTypeIdsException 발생 - URL: {}, Message: {}", request.getRequestURI(), ex.getMessage());
        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
