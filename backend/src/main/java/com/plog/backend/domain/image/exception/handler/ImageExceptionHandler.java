package com.plog.backend.domain.image.exception.handler;

import com.plog.backend.domain.image.exception.ImageNotFoundException;
import com.plog.backend.domain.image.exception.InvalidImageFileException;
import com.plog.backend.domain.image.exception.S3FileUploadException;
import com.plog.backend.global.exception.model.ExceptionResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ImageExceptionHandler {

    @ExceptionHandler(S3FileUploadException.class)
    public ResponseEntity<ExceptionResponseDto> handleS3FileUploadException(
            S3FileUploadException ex, HttpServletRequest request) {
        log.error("S3FileUploadException 발생 - URL: {}, Message: {}", request.getRequestURI(), ex.getMessage());
        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleImageNotFoundException(
            ImageNotFoundException ex, HttpServletRequest request) {
        log.error("ImageNotFoundException 발생 - URL: {}, Message: {}", request.getRequestURI(), ex.getMessage());
        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidImageFileException.class)
    public ResponseEntity<ExceptionResponseDto> handleInvalidImageFileException(
            InvalidImageFileException ex, HttpServletRequest request) {
        log.error("InvalidImageFileException 발생 - URL: {}, Message: {}", request.getRequestURI(), ex.getMessage());
        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}