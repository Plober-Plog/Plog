package com.plog.backend.domain.diary.entity;

import com.plog.backend.global.exception.NotValidRequestException;

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

    public static Weather weather(int value) {
        for (Weather weather : Weather.values()) {
            if (weather.getValue() == value) {
                return weather;
            }
        }
        throw new NotValidRequestException("Invalid value: " + value);
    }
}
