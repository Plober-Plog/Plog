package com.plog.backend.domain.diary.entity;

public enum Weather {
    SUNNY(1),
    CLOUDY(2),
    VERY_CLOUDY(3),
    RAINY(4);

    private int value;

    Weather(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
