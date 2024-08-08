package com.plog.realtime.domain.user.entity;

import com.plog.realtime.global.exception.NotValidRequestException;

public enum Role {
    USER(1),
    ADMIN(2);

    private int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Role role(int value) {
        for (Role role : Role.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new NotValidRequestException("Invalid value: " + value);
    }
}
