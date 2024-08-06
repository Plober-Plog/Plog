package com.plog.backend.domain.sns.entity;

import com.plog.backend.global.exception.NotValidRequestException;

public enum DeleteType {
    PLAIN(1),
    DELETE(2),
    BAN(3);

    private int value;

    DeleteType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DeleteType deleteType(int value) {
        for (DeleteType deleteType : DeleteType.values()) {
            if (deleteType.getValue() == value) {
                return deleteType;
            }
        }
        throw new NotValidRequestException("Invalid value: " + value);
    }
}
