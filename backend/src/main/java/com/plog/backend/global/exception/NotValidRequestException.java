package com.plog.backend.global.exception;

import lombok.Getter;

// 요청이 유효하지 않을 경우의 예외
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