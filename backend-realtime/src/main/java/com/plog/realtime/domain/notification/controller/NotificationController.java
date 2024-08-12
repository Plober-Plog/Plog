package com.plog.realtime.domain.notification.controller;

import com.plog.realtime.domain.notification.dto.NotificationHistoryResponseDto;
import com.plog.realtime.domain.notification.dto.NotificationMessageResponseDto;
import com.plog.realtime.domain.notification.entity.NotificationType;
import com.plog.realtime.domain.notification.service.NotificationService;
import com.plog.realtime.global.model.response.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "알림 전송", description = "알림을 특정 사용자에게 전송합니다.")
    @PostMapping("/send")
    public ResponseEntity<BaseResponseBody> sendNotification(
            @Parameter(description = "알림을 보낸 사람의 ID") @RequestParam String sourceSearchId,
            @Parameter(description = "알림을 받을 사람의 ID") @RequestParam String targetSearchId,
            @Parameter(description = "알림 클릭 시 이동할 URL") @RequestParam String clickUrl,
            @Parameter(description = "알림 유형") @RequestParam NotificationType type) {
        log.info("sendNotification 시작 - sourceSearchId: {}, targetSearchId: {}, type: {}", sourceSearchId, targetSearchId, type);
        notificationService.sendNotification(sourceSearchId, targetSearchId, clickUrl, type);
        log.info("sendNotification 완료 - sourceSearchId: {}, targetSearchId: {}, type: {}", sourceSearchId, targetSearchId, type);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "알림 전송이 완료되었습니다."));
    }

    @Operation(summary = "알림 이력 조회", description = "특정 사용자의 알림 이력을 조회합니다.")
    @GetMapping("/history")
    public List<NotificationHistoryResponseDto> getNotificationHistory(
            @Parameter(description = "사용자의 검색 ID") @RequestParam String searchId,
            @Parameter(description = "페이지 번호 : 0이 기본") @RequestParam(required = false, defaultValue = "0") String page) {
        log.info("getNotificationHistory 시작 - searchId: {}, page: {}", searchId, page);
        List<NotificationHistoryResponseDto> history = notificationService.getNotifications(searchId, Integer.parseInt(page));
        log.info("getNotificationHistory 완료 - searchId: {}, page: {}", searchId, page);
        return history;
    }

    @Operation(summary = "읽지 않은 알림 조회", description = "특정 사용자의 읽지 않은 알림 목록을 조회합니다.")
    @GetMapping("/unread")
    public List<NotificationMessageResponseDto> getUnreadNotifications(
            @Parameter(description = "사용자의 검색 ID") @RequestParam String searchId) {
        log.info("getUnreadNotifications 시작 - searchId: {}", searchId);
        List<NotificationMessageResponseDto> unreadNotifications = notificationService.getUnreadNotifications(searchId);
        log.info("getUnreadNotifications 완료 - searchId: {}", searchId);
        return unreadNotifications;
    }

    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음 상태로 처리합니다.")
    @PutMapping("/mark-as-read")
    public ResponseEntity<BaseResponseBody> markAsRead(
            @Parameter(description = "읽음 처리할 알림 ID") @RequestParam Long notificationId) {
        log.info("markAsRead 시작 - notificationId: {}", notificationId);
        notificationService.markAsRead(notificationId);
        log.info("markAsRead 완료 - notificationId: {}", notificationId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "알림 확인이 완료되었습니다."));
    }
}
