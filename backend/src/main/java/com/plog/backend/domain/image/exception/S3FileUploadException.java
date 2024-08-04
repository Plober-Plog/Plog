package com.plog.backend.domain.image.exception;

import lombok.Getter;

// S3업로드시 발생하는 예외
@Getter
public class S3FileUploadException extends RuntimeException {
    private final String message;

    public S3FileUploadException() {
        this.message = "S3 파일 업로드 중 오류가 발생했습니다.";
    }

    public S3FileUploadException(String message) {
        this.message = message;
    }
}
