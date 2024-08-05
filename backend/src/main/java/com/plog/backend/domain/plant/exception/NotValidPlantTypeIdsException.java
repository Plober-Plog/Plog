package com.plog.backend.domain.plant.exception;

import lombok.Getter;

// 식물의 종류가 유효하지 않을 경우의 예외
@Getter
public class NotValidPlantTypeIdsException extends RuntimeException {
    private final String message;

    public NotValidPlantTypeIdsException() {
        this.message = "유효하지 않은 식물 종류입니다.";
    }

    public NotValidPlantTypeIdsException(String message) {
        this.message = message;
    }
}
