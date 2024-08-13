package com.plog.realtime.domain.chat.repository;

import com.plog.realtime.domain.chat.dto.response.ChatRoomGetListResponseDto;
import com.plog.realtime.domain.chat.entity.ChatRoom;
import com.plog.realtime.domain.chat.entity.QChat;
import com.plog.realtime.domain.chat.entity.QChatRoom;
import com.plog.realtime.domain.chat.entity.QChatUser;
import com.plog.realtime.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatRoomRepositorySupport extends QuerydslRepositorySupport {
    private JPAQueryFactory queryFactory;

    public ChatRoomRepositorySupport(JPAQueryFactory queryFactory) {
        super(ChatRoomRepository.class);
        this.queryFactory = queryFactory;
    }

    public List<ChatRoom> findByChatUserId(Long chatUserId) {
        QChatRoom qChatRoom = QChatRoom.chatRoom;
        return queryFactory.selectFrom(qChatRoom)
                .where(qChatRoom.chatRoomId.eq(chatUserId))
                .where(qChatRoom.isDeleted.eq(false))
                .fetch();
    }

    public List<ChatRoom> findChatRoomsByUserId(Long userId) {
        QChatUser qChatUser = QChatUser.chatUser;
        QChatRoom qChatRoom = QChatRoom.chatRoom;

        // 사용자가 참여하고 있는 채팅방을 조회하는 쿼리
        List<ChatRoom> chatRoomList = queryFactory.select(qChatRoom)
                .from(qChatUser)
                .join(qChatUser.chatRoom, qChatRoom) // ChatUser와 ChatRoom을 조인
                .where(qChatUser.user.userId.eq(userId)) // 해당 userId에 대한 조건
                .fetch();

        return chatRoomList;
    }

    public List<User> findUsersByChatRoomId(Long chatRoomId) {
        QChat qChat = QChat.chat;

        List<User> users = queryFactory
                .select(qChat.user)
                .from(qChat)
                .where(qChat.chatRoom.chatRoomId.eq(chatRoomId))
                .distinct()  // 중복 제거
                .fetch();

        return users;
    }
}
