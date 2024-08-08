package com.plog.realtime.domain.chat.repository;


import com.plog.realtime.domain.chat.entity.ChatRoom;
import com.plog.realtime.domain.chat.entity.ChatUser;
import com.plog.realtime.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    Optional<ChatUser> findByUserAndChatRoom(User user, ChatRoom chatRoom);
}
