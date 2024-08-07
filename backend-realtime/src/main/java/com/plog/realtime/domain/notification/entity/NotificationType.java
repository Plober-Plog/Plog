package com.plog.realtime.domain.notification.entity;

import com.plog.realtime.global.exception.NotValidRequestException;

public enum NotificationType {
    COMMENT(1, "%s님이 %s님의 게시글에 댓글을 달았어요!", true, false), // 내 게시글에 댓글이 달린 경우
    LIKE(2, "%s님이 %s님의 게시글을 좋아해요!", true, false), // 내 게시글에 좋아요가 달린 경우
    BOOKMARK(3, "%s님이 %s님의 게시글을 북마크했어요!", true, false), // 내 게시글이 북마크된 경우
    POST_REPORTED(4, "%s님의 게시글이 신고로 인해 제재를 받았어요.", false, false), // 내 게시글이 신고로 인해 제재를 받은 경우
    COMMENT_REPORTED(5, "%s님의 댓글이 신고로 인해 제재를 받았어요.", false, false), // 내 댓글이 신고로 인해 제재를 받은 경우
    EVENT(6, "새로운 이벤트가 있습니다!(이벤트 메시지 뭔가 들어간다)", false, false), // 이벤트나 광고 알림
    NEIGHBOR_REQUEST(7, "%s님이 %s님에게 이웃 신청을 했어요!", true, false), // 누군가가 나에게 이웃 신청을 한 경우
    M_NEIGHBOR_REQUEST(8, "%s님이 %s님에게 서로 이웃 신청을 했어요!", true, false), // 누군가가 나에게 서로 이웃 신청을 한 경우
    CHAT_MESSAGE(9, "%s님이 %s님에게 채팅 메시지를 보냈어요!", true, false), // 채팅방에서 누군가가 나에게 채팅 메시지를 보낸 경우
    TRANSACTION_REQUEST(10, "%s님이 %s님에게 거래 신청을 보냈어요!", true, false), // 누군가가 나에게 거래 신청을 보낸 경우
    NEIGHBOR_REMOVED(11, "%s님이 %s님과의 이웃 관계를 끊었어요.", true, false), // 누군가가 나와의 이웃 관계를 끊은 경우
    M_NEIGHBOR_REMOVED(12, "%s님이 %s님과의 서로 이웃 관계를 끊었어요.", true, false), // 누군가가 나와의 서로 이웃 관계를 끊은 경우
    WATER_REMINDER(13, "%s님의 식물 %s에게 물을 줄 시간이에요.", false, true), // 내 식물의 물 주는 날짜 알림
    FERTILIZE_REMINDER(14, "%s님의 식물 %s에게 영양제를 줄 시간이에요.", false, true), // 내 식물의 영양제 주는 날짜 알림
    REPOT_REMINDER(15, "%s님의 식물 %s에게 분갈이를 해줄 시간이에요.", false, true); // 내 식물의 분갈이 날짜 알림

    private final int value;
    private final String defaultMessage;
    private final boolean requiresSource;
    private final boolean requiresPlant;

    NotificationType(int value, String defaultMessage, boolean requiresSource, boolean requiresPlant) {
        this.value = value;
        this.defaultMessage = defaultMessage;
        this.requiresSource = requiresSource;
        this.requiresPlant = requiresPlant;
    }

    public int getValue() {
        return value;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public boolean requiresSource() {
        return requiresSource;
    }

    public boolean requiresPlant() {
        return requiresPlant;
    }

    public static NotificationType notificationType(int value) {
        for (NotificationType notificationType : NotificationType.values()) {
            if (notificationType.getValue() == value) {
                return notificationType;
            }
        }
        throw new NotValidRequestException("Invalid value: " + value);
    }
}
