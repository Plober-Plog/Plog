package com.plog.realtime.domain.notification.service;

import com.plog.realtime.domain.notification.dto.NotificationHistoryResponseDto;
import com.plog.realtime.domain.notification.dto.NotificationMessageResponseDto;
import com.plog.realtime.domain.notification.entity.NotificationType;

import java.util.List;

public interface NotificationService {
    List<NotificationHistoryResponseDto> getNotifications(String searchId, int page);

    NotificationMessageResponseDto sendNotification(String sourceSearchId, String targetSearchId, String clickUrl, NotificationType type);

    void checkPlantNotifications();

    List<NotificationMessageResponseDto> getUnreadNotifications(String searchId);

    void markAsRead(Long notificationId);
}
