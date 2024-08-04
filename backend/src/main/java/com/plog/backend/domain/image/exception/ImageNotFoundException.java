package com.plog.backend.domain.image.exception;

import lombok.Getter;

// 이미지를 찾을 수 없을 경우의 예외
@Getter
public class ImageNotFoundException extends RuntimeException {
    private final String message;

    public ImageNotFoundException() {
        this.message = "해당 이미지를 찾을 수 없습니다.";
    }

    public ImageNotFoundException(String message) {
        this.message = message;
    }
}
