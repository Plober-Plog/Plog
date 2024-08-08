package com.plog.realtime.domain.chat.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ChatRoomUpdateRequestDto {
    private Long chatRoomId;
    private String chatRoomName;
}
