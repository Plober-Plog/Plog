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
    private Long userId;
    private Long chatRoomId;
    private String message;
    private String chatType;
    // 추가 데이터
    private String image;
    private String searchId;
    private String nickname;
    LocalDateTime createdAt;
}
