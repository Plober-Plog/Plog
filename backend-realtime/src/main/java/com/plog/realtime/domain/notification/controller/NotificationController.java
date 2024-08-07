package com.plog.realtime.domain.notification.controller;

import com.plog.realtime.domain.notification.dto.NotificationMessageResponseDto;
import com.plog.realtime.domain.notification.entity.NotificationType;
import com.plog.realtime.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final NotificationService notificationService;
    private static final long SSE_TIMEOUT = 30 * 60 * 1000L; // 30분

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam String searchId) {
        log.info("subscribe 시작 - searchId: {}", searchId);
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        emitters.add(emitter);

        emitter.onCompletion(() -> {
            emitters.remove(emitter);
            log.info("Emitter completed: {}", emitter);
        });

        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            log.info("Emitter timeout: {}", emitter);
        });

        emitter.onError((e) -> {
            emitters.remove(emitter);
            log.error("Emitter error: {}", emitter, e);
        });

        // 클라이언트가 처음 구독할 때 전송되지 않은 알림을 전송
        List<NotificationMessageResponseDto> unsentNotifications = notificationService.getUnsentNotifications(searchId);
        unsentNotifications.forEach(notification -> {
            try {
                emitter.send(SseEmitter.event().name("notification").data(notification));
                notificationService.markAsSent(notification.getNotificationId());
            } catch (IOException e) {
                log.error("Error sending unsent notification: {}", e.getMessage());
            }
        });

        log.info("subscribe 완료 - searchId: {}", searchId);
        return emitter;
    }

    @PostMapping("/send")
    public void sendNotification(@RequestParam String sourceSearchId, @RequestParam String targetSearchId, @RequestParam NotificationType type) {
        log.info("sendNotification 시작 - sourceSearchId: {}, targetSearchId: {}, type: {}", sourceSearchId, targetSearchId, type);
        NotificationMessageResponseDto notification = notificationService.sendNotification(sourceSearchId, targetSearchId, type);
        sendSseEvent(notification);
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

    public void sendSseEvent(NotificationMessageResponseDto notification) {
        log.info("sendSseEvent 시작 - notificationId: {}", notification.getNotificationId());
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(notification));
                notificationService.markAsSent(notification.getNotificationId());
            } catch (IOException e) {
                log.info("Error sending notification: {}", e.getMessage());
                emitters.remove(emitter);
            }
        }
        log.info("sendSseEvent 완료 - notificationId: {}", notification.getNotificationId());
    }
}
