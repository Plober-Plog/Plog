package com.plog.backend.domain.sns.entity;

import com.plog.backend.global.exception.NotValidRequestException;

public enum State {
    PLAIN(1),
    DELETE(2),
    BAN(3);

    private int value;

    State(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static State state(int value) {
        for (State state : State.values()) {
            if (state.getValue() == value) {
                return state;
            }
        }
        throw new NotValidRequestException("Invalid value: " + value);
    }
}
