package com.plog.backend.global.exception;

import lombok.Getter;

// TODO [강윤서] 어떤 exception을 생각하고 만들었는지 써주기
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