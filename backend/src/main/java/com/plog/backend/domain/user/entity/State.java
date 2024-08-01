package com.plog.backend.domain.user.entity;

public enum State {
    ACTIVTE(1),
    DELETED(2),
    BANNED(3);

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
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
