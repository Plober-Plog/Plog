package com.plog.backend.domain.plant.exception;

import lombok.Getter;

@Getter
public class NotValidPlantTypeIdsException extends RuntimeException {
    private final String message;

    public NotValidPlantTypeIdsException() {
        this.message = "유효한 식물 종류가 아닙니다.";
    }

    public NotValidPlantTypeIdsException(String message) {
        this.message = message;
    }
}
