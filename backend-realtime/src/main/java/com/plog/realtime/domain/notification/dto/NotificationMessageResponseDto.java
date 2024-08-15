package com.plog.realtime.domain.notification.dto;

import com.plog.realtime.domain.notification.entity.Notification;
import com.plog.realtime.domain.notification.entity.NotificationType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class NotificationMessageResponseDto {
    private Long notificationId;
    private String type;
    private boolean isRead;
    private String content;

    public static NotificationMessageResponseDto toNotificationDTO(Notification notification) {
        return new NotificationMessageResponseDto(
                notification.getNotificationId(),
                NotificationType.notificationType(notification.getType()).name(),
                notification.getIsRead(),
                notification.getContent()
        );
    }
}
