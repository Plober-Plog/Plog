package com.plog.backend.domain.user.exception;

import lombok.Getter;

@Getter
public class InvalidEmailFormatException extends RuntimeException {

    private final String message;

    public InvalidEmailFormatException() {
        this.message = "이메일 형식이 맞지 않습니다.";
    }

    public InvalidEmailFormatException(String message) {
        this.message = message;
    }
}
