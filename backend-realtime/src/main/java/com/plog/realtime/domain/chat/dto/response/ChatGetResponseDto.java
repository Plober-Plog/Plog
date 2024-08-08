package com.plog.realtime.domain.chat.dto.response;

import java.time.LocalDateTime;

public class ChatGetResponseDto {
    String nickname;
    String profile;
    String message;
    int chatType;
    LocalDateTime createdAt;
}
