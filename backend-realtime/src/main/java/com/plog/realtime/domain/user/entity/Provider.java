package com.plog.realtime.domain.user.entity;

import com.plog.realtime.global.exception.NotValidRequestException;

public enum Provider {
    KAKAO(1),
    GOOGLE(2),
    NAVER(3);

    private int value;

    Provider(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Provider provider(int value) {
        for (Provider provider : Provider.values()) {
            if (provider.getValue() == value) {
                return provider;
            }
        }
        throw new NotValidRequestException("Invalid value: " + value);
    }
}
