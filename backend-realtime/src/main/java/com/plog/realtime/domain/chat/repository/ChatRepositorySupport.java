package com.plog.realtime.domain.chat.repository;

import com.plog.realtime.domain.chat.entity.Chat;
import com.plog.realtime.domain.chat.entity.QChat;
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

        // 모든 채팅 메시지를 최신순으로 정렬한 후
        List<Chat> sortedChats = queryFactory.selectFrom(chat)
                .where(chat.chatRoom.chatRoomId.eq(chatRoomId))
                .orderBy(chat.createdAt.asc())
                .fetch();

        // 정렬된 리스트에서 페이지네이션 적용
        List<Chat> filteredArticleList = sortedChats.stream()
                .sorted((a1, a2) -> {
                        return a2.getCreatedAt().compareTo(a1.getCreatedAt());
                })
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toList());

        return filteredArticleList;
    }
}
