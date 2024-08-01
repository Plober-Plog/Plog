package com.plog.backend.domain.user.entity;

public enum Gender {
    NA(1),
    MALE(2),
    FEMALE(3);

    private int value;

    Gender(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public static Gender gender(int value) {
        for (Gender gender : Gender.values()) {
            if (gender.getValue() == value) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
