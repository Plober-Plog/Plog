package com.plog.realtime.domain.notification.service;

import com.plog.realtime.domain.notification.dto.NotificationMessageResponseDto;
import com.plog.realtime.domain.notification.entity.NotificationType;

import java.util.List;

public interface NotificationService {
    List<NotificationMessageResponseDto> getNotifications(String searchId);

    NotificationMessageResponseDto sendNotification(String sourceSearchId, String targetSearchId, NotificationType type);

    void checkPlantNotifications();

    List<NotificationMessageResponseDto> getUnreadNotifications(String searchId);

    void markAsRead(Long notificationId);
}
