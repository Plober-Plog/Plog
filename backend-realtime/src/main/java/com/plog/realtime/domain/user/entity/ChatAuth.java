package com.plog.realtime.domain.user.entity;

import com.plog.realtime.global.exception.NotValidRequestException;

public enum ChatAuth {
    PUBLIC(1),
    NEIGHBOR(2),
    MUTUAL_NEIGHBOR(3),
    PRIVATE(4);

    private int value;

    ChatAuth(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ChatAuth chatAuth(int value) {
        for (ChatAuth chatAuth : ChatAuth.values()) {
            if (chatAuth.getValue() == value) {
                return chatAuth;
            }
        }
        throw new NotValidRequestException("Invalid value: " + value);
    }
}
