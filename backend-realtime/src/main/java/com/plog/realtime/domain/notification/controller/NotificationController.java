package com.plog.realtime.domain.notification.controller;

import com.plog.realtime.domain.notification.dto.NotificationHistoryResponseDto;
import com.plog.realtime.domain.notification.dto.NotificationMessageResponseDto;
import com.plog.realtime.domain.notification.entity.NotificationType;
import com.plog.realtime.domain.notification.service.NotificationService;
import com.plog.realtime.global.model.response.BaseResponseBody;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<BaseResponseBody> sendNotification(@RequestParam String sourceSearchId,
                                                             @RequestParam String targetSearchId,
                                                             @RequestParam String clickUrl,
                                                             @RequestParam NotificationType type) {
        log.info("sendNotification 시작 - sourceSearchId: {}, targetSearchId: {}, type: {}", sourceSearchId, targetSearchId, type);
        notificationService.sendNotification(sourceSearchId, targetSearchId, clickUrl, type);
        log.info("sendNotification 완료 - sourceSearchId: {}, targetSearchId: {}, type: {}", sourceSearchId, targetSearchId, type);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "알림 전송이 완료되었습니다."));
    }

    @GetMapping("/history")
    public List<NotificationHistoryResponseDto> getNotificationHistory(
            @Parameter(description = "searchId") @RequestParam String searchId,
            @Parameter(description = "페이지 번호 : 0이 기본") @RequestParam(required = false, defaultValue = "0") String page) {
        log.info("getNotificationHistory 시작 - searchId: {}, page: {}", searchId, page);
        List<NotificationHistoryResponseDto> history = notificationService.getNotifications(searchId, Integer.parseInt(page));
        log.info("getNotificationHistory 완료 - searchId: {}, page: {}", searchId, page);
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
    public ResponseEntity<BaseResponseBody> markAsRead(@RequestParam Long notificationId) {
        log.info("markAsRead 시작 - notificationId: {}", notificationId);
        notificationService.markAsRead(notificationId);
        log.info("markAsRead 완료 - notificationId: {}", notificationId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "알림 확인이 완료되었습니다."));
    }
}
