package com.plog.realtime.domain.notification.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plog.realtime.domain.notification.controller.NotificationController;
import com.plog.realtime.domain.notification.dto.NotificationMessageResponseDto;
import com.plog.realtime.domain.notification.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationListener implements MessageListener {

    private final NotificationController notificationController;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 메시지를 Notification 객체로 변환
        NotificationMessageResponseDto notification = null;
        try {
            notification = objectMapper.readValue(message.getBody(), NotificationMessageResponseDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        notificationController.sendSseEvent(notification);
    }
}
