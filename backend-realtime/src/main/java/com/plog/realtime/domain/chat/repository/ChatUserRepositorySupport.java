package com.plog.realtime.domain.chat.repository;

import com.plog.realtime.domain.chat.entity.ChatUser;
import com.plog.realtime.domain.chat.entity.QChatUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class ChatUserRepositorySupport extends QuerydslRepositorySupport {
    private JPAQueryFactory queryFactory;

    public ChatUserRepositorySupport(JPAQueryFactory queryFactory) {
        super(ChatUser.class);
        this.queryFactory = queryFactory;
    }

    public boolean areBothUsersInSameChatRoom(Long userId1, Long userId2) {
        QChatUser chatUser1 = QChatUser.chatUser;
        QChatUser chatUser2 = new QChatUser("chatUser2");

        long count = queryFactory.selectFrom(chatUser1)
                .join(chatUser2).on(chatUser1.chatRoom.eq(chatUser2.chatRoom))
                .where(chatUser1.user.userId.eq(userId1)
                        .and(chatUser2.user.userId.eq(userId2)))
                .fetchCount();

        return count > 0;
    }
}
