package com.plog.realtime.domain.notification.repository;

import com.plog.realtime.domain.notification.dto.NotificationHistoryResponseDto;
import com.plog.realtime.domain.notification.entity.Notification;
import com.plog.realtime.domain.notification.entity.QNotification;
import com.plog.realtime.domain.user.entity.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class NotificationRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final int size = 20;

    public NotificationRepositorySupport(JPAQueryFactory queryFactory) {
        super(Notification.class);
        this.queryFactory = queryFactory;
    }

    public List<NotificationHistoryResponseDto> findByUserSearchId(String searchId, int page) {
        QNotification notification = QNotification.notification;
        QUser user = QUser.user;

        List<Tuple> results = queryFactory.select(
                        notification.notificationId,
                        notification.type,
                        notification.content,
                        notification.isRead,
                        notification.clickUrl,
                        notification.createdAt
                )
                .from(notification)
                .leftJoin(notification.user, user)
                .where(user.searchId.eq(searchId))
                .orderBy(notification.createdAt.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        return results.stream()
                .map(tuple -> NotificationHistoryResponseDto.builder()
                        .notificationId(tuple.get(notification.notificationId))
                        .type(String.valueOf(tuple.get(notification.type)))
                        .content(tuple.get(notification.content))
                        .isRead(tuple.get(notification.isRead))
                        .clickUrl(tuple.get(notification.clickUrl))
                        .notificationDate(tuple.get(notification.createdAt).toLocalDate())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
