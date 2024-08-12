package com.plog.realtime.domain.chat.repository;

import com.plog.realtime.domain.chat.entity.ChatRoom;
import com.plog.realtime.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByChatRoomId(Long chatRoomId);

    Optional<ChatRoom> findByChatRoomIdAndUser(Long chatRoomId, User user);
}
