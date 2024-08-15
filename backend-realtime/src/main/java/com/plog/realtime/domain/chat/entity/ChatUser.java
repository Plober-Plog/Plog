package com.plog.realtime.domain.chat.entity;

import com.plog.realtime.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ChatUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatUserId;

    @ManyToOne
    @JoinColumn(name="chat_room_id", referencedColumnName = "chatRoomId")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "userId")
    private User user;

    // 추가: 사용자가 마지막으로 읽은 메시지의 시간
    private LocalDateTime lastReadAt;
}
