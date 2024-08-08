package com.plog.realtime.domain.chat.entity;

import com.plog.realtime.domain.user.entity.User;
import com.plog.realtime.global.model.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", referencedColumnName = "chatRoomId")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @Column
    String message;

}
