package com.plog.realtime.domain.chat.repository;


import com.plog.realtime.domain.chat.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    Optional<ChatUser> findByUserIdAndChatRoomId(Long userId, Long chatRoomId);
}
