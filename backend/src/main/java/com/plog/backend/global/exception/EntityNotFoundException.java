package com.plog.backend.global.exception;

// TODO [강윤서] 어떤 exception을 생각하고 만들었는지 써주기
public class EntityNotFoundException extends RuntimeException {
    private final String message;

    public EntityNotFoundException() {
        this.message = "일치하는 entity를 찾을 수 없습니다.";
    }

    public EntityNotFoundException(String message) {
        this.message = message;
    }
}