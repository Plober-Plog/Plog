package com.plog.realtime.domain.notification.controller;

import com.plog.realtime.domain.notification.dto.NotificationMessageResponseDto;
import com.plog.realtime.domain.notification.entity.NotificationType;
import com.plog.realtime.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public void sendNotification(@RequestParam String sourceSearchId, @RequestParam String targetSearchId, @RequestParam NotificationType type) {
        log.info("sendNotification 시작 - sourceSearchId: {}, targetSearchId: {}, type: {}", sourceSearchId, targetSearchId, type);
        NotificationMessageResponseDto notification = notificationService.sendNotification(sourceSearchId, targetSearchId, type);
        log.info("sendNotification 완료 - sourceSearchId: {}, targetSearchId: {}, type: {}", sourceSearchId, targetSearchId, type);
    }

    @GetMapping("/history")
    public List<NotificationMessageResponseDto> getNotificationHistory(@RequestParam String searchId) {
        log.info("getNotificationHistory 시작 - searchId: {}", searchId);
        List<NotificationMessageResponseDto> history = notificationService.getNotifications(searchId);
        log.info("getNotificationHistory 완료 - searchId: {}", searchId);
        return history;
    }

    @GetMapping("/unread")
    public List<NotificationMessageResponseDto> getUnreadNotifications(@RequestParam String searchId) {
        log.info("getUnreadNotifications 시작 - searchId: {}", searchId);
        List<NotificationMessageResponseDto> unreadNotifications = notificationService.getUnreadNotifications(searchId);
        log.info("getUnreadNotifications 완료 - searchId: {}", searchId);
        return unreadNotifications;
    }

    @PutMapping("/mark-as-read")
    public void markAsRead(@RequestParam Long notificationId) {
        log.info("markAsRead 시작 - notificationId: {}", notificationId);
        notificationService.markAsRead(notificationId);
        log.info("markAsRead 완료 - notificationId: {}", notificationId);
    }
}
