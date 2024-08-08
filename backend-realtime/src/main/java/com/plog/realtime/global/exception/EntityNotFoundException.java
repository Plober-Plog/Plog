package com.plog.realtime.global.exception;

// 일치하는 entity를 조회할 수 없을 경우의 예외
public class EntityNotFoundException extends RuntimeException {
    private final String message;

    public EntityNotFoundException() {
        this.message = "일치하는 entity를 찾을 수 없습니다.";
    }

    public EntityNotFoundException(String message) {
        this.message = message;
    }
}