package com.plog.realtime.domain.chat.entity;

import com.plog.realtime.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

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
}
