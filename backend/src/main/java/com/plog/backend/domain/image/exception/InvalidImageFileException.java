package com.plog.backend.domain.image.exception;

import lombok.Getter;

// 유효하지 않은 이미지 파일인 경우의 예외
@Getter
public class InvalidImageFileException extends RuntimeException {
    private final String message;

    public InvalidImageFileException() {
        this.message = "유효하지 않은 이미지 파일입니다.";
    }

    public InvalidImageFileException(String message) {
        this.message = message;
    }
}