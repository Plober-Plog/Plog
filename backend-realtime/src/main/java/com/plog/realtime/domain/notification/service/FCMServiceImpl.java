package com.plog.realtime.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FCMServiceImpl implements FCMService {

    @Override
    public void sendNotification(String token, String title, String message) {
        Message msg = Message.builder()
                .putData("message", message)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(message)
                        .build()) // notification 필드를 사용하여 title과 message를 설정
                .setToken(token)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(msg);
            log.info("FCM 메시지 전송 성공: " + response);
        } catch (Exception e) {
            log.error("FCM 메시지 전송 실패: ", e);
        }
    }
}
