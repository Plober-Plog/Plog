package com.plog.realtime.domain.chat.repository;


import com.plog.realtime.domain.chat.entity.ChatRoom;
import com.plog.realtime.domain.chat.entity.ChatUser;
import com.plog.realtime.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    Optional<ChatUser> findFirstByUserAndChatRoom(User user, ChatRoom chatRoom);
    Optional<ChatUser> findByUserUserIdAndChatRoomChatRoomId(Long userId, Long chatRoomId);
}
