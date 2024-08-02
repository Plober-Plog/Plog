package com.plog.backend.global.exception;

public class EntityNotFoundException extends RuntimeException {
    private final String message;

    public EntityNotFoundException() {
        this.message = "일치하는 entity를 찾을 수 없습니다.";
    }

    public EntityNotFoundException(String message) {
        this.message = message;
    }
}