package com.plog.realtime.domain.chat.repository;

import com.plog.realtime.domain.chat.entity.Chat;
import com.plog.realtime.domain.chat.entity.ChatRoom;
import com.plog.realtime.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findTopByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);
}
