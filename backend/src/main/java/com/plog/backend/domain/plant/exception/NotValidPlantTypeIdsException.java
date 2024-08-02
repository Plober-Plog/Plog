package com.plog.backend.domain.plant.exception;

import lombok.Getter;

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
