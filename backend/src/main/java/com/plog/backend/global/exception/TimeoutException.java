package com.plog.backend.global.exception;

import lombok.Getter;

// TODO [장현준] JWT Token이 만료되거나, 인증 메일이 만료되었을 때
@Getter
public class TimeoutException extends RuntimeException {
    private final String message;

    public TimeoutException() {
        this.message = "만료된 요청입니다.";
    }

    public TimeoutException(String message) {
        this.message = message;
    }
}