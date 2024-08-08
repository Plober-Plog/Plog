package com.plog.realtime.domain.chat.repository;

import com.plog.realtime.domain.chat.entity.Chat;
import com.plog.realtime.domain.chat.entity.QChat;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatRepositorySupport extends QuerydslRepositorySupport {
    private JPAQueryFactory queryFactory;
    private int size = 30;

    public ChatRepositorySupport(JPAQueryFactory paQueryFactory) {
        super(Chat.class);
        this.queryFactory = paQueryFactory;
    }
    public List<Chat> findChatsByChatRoomId(Long chatRoomId, int page) {
        QChat chat = QChat.chat;

        return queryFactory.selectFrom(chat)
                .where(chat.chatRoom.chatRoomId.eq(chatRoomId))
                .orderBy(chat.createdAt.desc())
                .offset(page * size)
                .limit(size)
                .fetch();
    }
}
