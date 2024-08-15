package com.plog.realtime.domain.chat.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ChatRoomCreateRequestDto {
    private String targetSearchId;
    private int chatRoomType;
    private String chatRoomName;
}
