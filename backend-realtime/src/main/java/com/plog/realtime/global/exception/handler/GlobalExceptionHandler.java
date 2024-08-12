package com.plog.realtime.global.exception.handler;

import com.plog.realtime.global.exception.EntityNotFoundException;
import com.plog.realtime.global.exception.NotAuthorizedRequestException;
import com.plog.realtime.global.exception.TimeoutException;
import com.plog.realtime.global.exception.model.ExceptionResponseDto;
import com.plog.realtime.global.exception.NotValidRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponseDto> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        log.error("EntityNotFoundException realtime 발생 - URL: {}, Message: {}", request.getRequestURI(), ex.getMessage());
        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotValidRequestException.class)
    public ResponseEntity<ExceptionResponseDto> handleNotValidRequestException(
            NotValidRequestException ex, HttpServletRequest request) {
        log.error("NotValidRequestException realtime 발생 - URL: {}, Message: {}", request.getRequestURI(), ex.getMessage());
        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAuthorizedRequestException.class)
    public ResponseEntity<ExceptionResponseDto> handleNotAuthorizedRequestException(
            NotAuthorizedRequestException ex, HttpServletRequest request) {
        log.error("NotAuthorizedRequestException realtime 발생 - URL: {}, Message: {}", request.getRequestURI(), ex.getMessage());
        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ExceptionResponseDto> handleTimeoutException(
            TimeoutException ex, HttpServletRequest request) {
        log.error("TimeoutException realtime 발생 - URL: {}, Message: {}", request.getRequestURI(), ex.getMessage());
        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.REQUEST_TIMEOUT.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleUnknownException(
            Exception ex, HttpServletRequest request) {
        log.error("Exception realtime 발생 - URL: {}, Message: {}", request.getRequestURI(), ex.getMessage());
        ex.printStackTrace();
        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
