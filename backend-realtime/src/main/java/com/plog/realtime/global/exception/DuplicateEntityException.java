package com.plog.realtime.global.exception;

import lombok.Getter;

@Getter
public class DuplicateEntityException extends RuntimeException {

    private final String message;

    public DuplicateEntityException() {
        this.message = "중복 가입입니다.";
    }

    public DuplicateEntityException(String message) {
        this.message = message;
    }
}
