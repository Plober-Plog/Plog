package com.plog.realtime.domain.chat.repository;

import com.plog.realtime.domain.chat.entity.ChatRoom;
import com.plog.realtime.domain.chat.entity.QChatRoom;
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
}
