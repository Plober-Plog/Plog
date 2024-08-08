package com.plog.realtime.domain.chat.dto.response;

import com.plog.realtime.domain.chat.entity.ChatType;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ChatGetResponseDto {
    String nickname;
    String profile;
    String message;
    ChatType chatType;
    LocalDateTime createdAt;
}
