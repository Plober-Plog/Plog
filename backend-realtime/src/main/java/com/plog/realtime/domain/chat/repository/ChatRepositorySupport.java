package com.plog.realtime.domain.chat.repository;

import com.plog.realtime.domain.chat.entity.Chat;
import com.plog.realtime.domain.chat.entity.QChat;
import com.plog.realtime.domain.user.entity.QUser;
import com.plog.realtime.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ChatRepositorySupport extends QuerydslRepositorySupport {
    private JPAQueryFactory queryFactory;
    private int size = 20;

    public ChatRepositorySupport(JPAQueryFactory paQueryFactory) {
        super(Chat.class);
        this.queryFactory = paQueryFactory;
    }

    public List<Chat> findChatsByChatRoomId(Long chatRoomId, int page) {
        QChat chat = QChat.chat;

        // 최신순으로 정렬하고 페이지네이션을 적용하여 쿼리 실행
        List<Chat> paginatedChats = queryFactory.selectFrom(chat)
                .where(
                        chat.chatRoom.chatRoomId.eq(chatRoomId)
                )
                .orderBy(chat.createdAt.desc()) // 최신순으로 정렬
                .offset(page * size)             // 페이지 시작점
                .limit(size)                     // 페이지 크기만큼 데이터 가져오기
                .fetch();

        return paginatedChats;
    }
}
