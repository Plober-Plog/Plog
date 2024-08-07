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
        log.info("Subscribing with searchId: {}", searchId);
//        SseEmitter emitter = new SseEmitter(60 * 1000L);
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
            } catch (IOException e) {
                log.error("Error sending unsent notification: {}", e.getMessage());
            }
        });

        return emitter;
    }

    @PostMapping("/send")
    public void sendNotification(@RequestParam String sourceSearchId, @RequestParam String targetSearchId, @RequestParam NotificationType type) {
        log.info("Sending notification: {} {} {} ", sourceSearchId, targetSearchId, type);
        NotificationMessageResponseDto notification = notificationService.sendNotification(sourceSearchId, targetSearchId, type);
        sendSseEvent(notification);
    }

    @GetMapping("/history")
    public List<NotificationMessageResponseDto> getNotificationHistory(@RequestParam String searchId) {
        return notificationService.getNotifications(searchId);
    }

    @GetMapping("/unread")
    public List<NotificationMessageResponseDto> getUnreadNotifications(@RequestParam String searchId) {
        return notificationService.getUnreadNotifications(searchId);
    }

    @PutMapping("/mark-as-read")
    public void markAsRead(@RequestParam Long notificationId) {
        notificationService.markAsRead(notificationId);
    }

    public void sendSseEvent(NotificationMessageResponseDto notification) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(notification));
                notificationService.markAsSent(notification.getNotificationId());
            } catch (IOException e) {
                log.info("Error sending notification: {}", e.getMessage());
                emitters.remove(emitter);
            }
        }
    }
}
