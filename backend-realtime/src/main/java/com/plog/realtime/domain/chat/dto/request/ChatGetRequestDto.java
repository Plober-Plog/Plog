package com.plog.realtime.domain.chat.dto.request;

import java.time.LocalDateTime;

public class ChatGetRequestDto {
    String nickname;
    String profile;
    String message;
    int chatType;
    LocalDateTime createdAt;
}
