package com.plog.realtime.domain.chat.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ChatGetListRequestDto {
    private Long userId;
    private Long chatRoomId;
    private int page;
}
