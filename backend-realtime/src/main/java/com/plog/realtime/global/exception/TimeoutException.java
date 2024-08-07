package com.plog.realtime.global.exception;

import lombok.Getter;

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