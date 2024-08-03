package com.plog.backend.global.exception;

import lombok.Getter;

@Getter
public class NotAuthorizedRequestException extends RuntimeException {
    private final String message;

    public NotAuthorizedRequestException() {
        this.message = "요청을 처리할 권한이 없습니다.";
    }

    public NotAuthorizedRequestException(String message) {
        this.message = message;
    }
}
