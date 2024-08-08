package com.plog.realtime.domain.chat.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ChatType {
    JOIN(1),
    SEND(2),
    LEAVE(3);

    private final int value;
    ChatType(int value) {
        this.value = value;
    }
}
