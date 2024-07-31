package com.plog.backend.domain.plant.exception;

import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Getter
public class NotValidPlantTypeIdsException extends RuntimeException {
    private final HttpMethod httpMethod;
    private final HttpStatus httpStatus;
    private final String requestURL;

    public NotValidPlantTypeIdsException(HttpMethod httpMethod, HttpStatus httpStatus, String requestURL) {
        super("유효한 식물 종류가 아닙니다.");
        this.httpMethod = httpMethod;
        this.httpStatus = httpStatus;
        this.requestURL = requestURL;
    }
}
