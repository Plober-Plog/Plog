package com.plog.realtime.domain.chat.dto.response;

import com.plog.realtime.domain.chat.entity.Chat;
import com.plog.realtime.domain.chat.entity.ChatRoom;
import com.plog.realtime.domain.user.entity.User;
import lombok.*;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class ChatRoomGetListResponseDto {
    private ChatRoom chatRoom;
    private List<User> users; // 여러 참가자를 저장하기 위해 List로 변경
    private Chat lastChat;
    private boolean isRead;
}
