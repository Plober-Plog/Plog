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
    Long userId;
    Long chatRoomId;
    String nickname;
    String profile;
    String message;
    LocalDateTime createdAt;
}
