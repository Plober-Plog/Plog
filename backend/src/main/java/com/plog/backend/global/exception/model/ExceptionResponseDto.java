package com.plog.backend.global.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionResponse {
    private String httpMethod;
    private String requestURL;
    private int httpStatus;
    private String message;
    private LocalDateTime timestamp;

    public ExceptionResponse(String httpMethod, String requestURL, int httpStatus, String message, LocalDateTime timestamp) {
        this.httpMethod = httpMethod;
        this.requestURL = requestURL;
        this.httpStatus = httpStatus;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static ExceptionResponse of(String httpMethod, String requestURL, int httpStatus, String message) {
        return new ExceptionResponse(httpMethod, requestURL, httpStatus, message, LocalDateTime.now());
    }
}
