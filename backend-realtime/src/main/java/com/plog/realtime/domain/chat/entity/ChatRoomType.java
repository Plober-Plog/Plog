package com.plog.realtime.domain.chat.entity;

import java.util.NoSuchElementException;

public enum ChatRoomType {
    DIRECT_MESSAGE(1),
    GROUP_MESSAGE(2);

    private int value;

    ChatRoomType(int value) {this.value = value;}

    public int getValue() {return value;}

    public static ChatRoomType chatRoomType(int value) {
        for (ChatRoomType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new NoSuchElementException("일치하는 채팅방 종류가 없습니다.");
    }
}
