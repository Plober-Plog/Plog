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
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "userId")
    private User user;

    @Column
    private int chatRoomType;
    public ChatRoomType getChatRoomType() {
        return ChatRoomType.chatRoomType(chatRoomType);
    }
    public void setChatRoomType(ChatRoomType chatRoomType) {
        this.chatRoomType = chatRoomType.getValue();
    }

    @Column
    private String chatRoomName;

    @Column
    private boolean isDeleted;
}
