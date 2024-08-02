package com.plog.backend.global.exception;

import lombok.Getter;

@Getter
public class NotValidRequestException extends RuntimeException {
    private final String message;

    public NotValidRequestException() {
        this.message = "유효하지 않은 요청입니다.";
    }

    public NotValidRequestException(String message) {
        this.message = message;
    }
}