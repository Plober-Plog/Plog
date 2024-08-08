package com.plog.realtime.domain.chat.dto.request;

import com.plog.realtime.domain.chat.entity.ChatType;
import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ChatGetRequestDto {
    String nickname;
    String profile;
    String message;
    ChatType chatType;
    LocalDateTime createdAt;
}
