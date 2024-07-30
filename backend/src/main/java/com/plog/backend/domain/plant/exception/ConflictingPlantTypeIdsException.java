package com.plog.backend.domain.plant.exception;

public class ConflictingPlantTypeIdsException extends RuntimeException {
    public ConflictingPlantTypeIdsException() {
        super("적절한 식물 종류가 아닙니다.");
    }
    public ConflictingPlantTypeIdsException(String message) {
        super(message);
    }
}
