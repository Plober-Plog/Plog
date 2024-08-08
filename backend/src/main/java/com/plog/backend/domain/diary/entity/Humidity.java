package com.plog.backend.domain.diary.entity;

import com.plog.backend.global.exception.NotValidRequestException;

public enum Humidity {
    DRY(1),
    CLEAN(2),
    NORMAL(3),
    MOIST(4),
    WET(5);

    private int value;

    Humidity(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Humidity humidity(int value) {
        for (Humidity humidity : Humidity.values()) {
            if (humidity.getValue() == value) {
                return humidity;
            }
        }
        throw new NotValidRequestException("Invalid value: " + value);
    }
}
