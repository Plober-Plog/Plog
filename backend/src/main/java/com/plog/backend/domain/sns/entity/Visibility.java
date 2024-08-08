package com.plog.backend.domain.sns.entity;

import com.plog.backend.global.exception.NotValidRequestException;

public enum Visibility {
    PUBLIC(1),
    NEIGHBOR(2),
    MUTUAL_NEIGHBOR(3),
    PRIVATE(4);

    private int value;

    Visibility(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Visibility visibility(int value) {
        for (Visibility visibility : Visibility.values()) {
            if (visibility.getValue() == value) {
                return visibility;
            }
        }
        throw new NotValidRequestException("Invalid value: " + value);
    }
}