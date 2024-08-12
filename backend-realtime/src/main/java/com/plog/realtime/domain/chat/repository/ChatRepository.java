package com.plog.realtime.domain.chat.repository;

import com.plog.realtime.domain.chat.entity.Chat;
import com.plog.realtime.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findTopByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);
}
