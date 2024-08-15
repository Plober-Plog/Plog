package com.plog.realtime.domain.chat.repository;

import com.plog.realtime.domain.chat.entity.ChatRoom;
import com.plog.realtime.domain.chat.entity.ChatUser;
import com.plog.realtime.domain.chat.entity.QChatUser;
import com.plog.realtime.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ChatUserRepositorySupport extends QuerydslRepositorySupport {
    private JPAQueryFactory queryFactory;

    public ChatUserRepositorySupport(JPAQueryFactory queryFactory) {
        super(ChatUser.class);
        this.queryFactory = queryFactory;
    }

    public Optional<ChatRoom> findCommonChatRooms(Long userId1, Long userId2) {
        QChatUser chatUser1 = QChatUser.chatUser;
        QChatUser chatUser2 = new QChatUser("chatUser2");

        return Optional.ofNullable(queryFactory.select(chatUser1.chatRoom)
                .from(chatUser1)
                .join(chatUser2).on(chatUser1.chatRoom.eq(chatUser2.chatRoom))
                .where(chatUser1.user.userId.eq(userId1)
                        .and(chatUser2.user.userId.eq(userId2)))
                .fetchOne());
    }

    public List<User> findUsersByChatRoomId(Long chatRoomId) {
        QChatUser qChatUser = QChatUser.chatUser;

        List<User> users = queryFactory
                .select(qChatUser.user)
                .from(qChatUser)
                .where(qChatUser.chatRoom.chatRoomId.eq(chatRoomId))
                .distinct()  // 중복 제거
                .fetch();

        return users;
    }
}
