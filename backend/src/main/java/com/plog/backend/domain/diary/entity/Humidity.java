package com.plog.backend.domain.diary.entity;

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
}
