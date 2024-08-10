package com.plog.realtime.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FCMServiceImpl implements FCMService {

    @Override
    public void sendNotification(String token, String title, String clickUrl, String message, Long notificationId) {

        String iconUrl = "https://plogbucket.s3.ap-northeast-2.amazonaws.com/free-icon-sprout-267205.png";

        // Notification notification = Notification.builder()
        //         .setTitle(title)
        //         .setBody(message)
        //         .build();

        Message msg = Message.builder()
                // .setNotification(notification)
                .putData("title", title)
                .putData("message", message)
                .putData("click_action", clickUrl) // 데이터 페이로드에 클릭 액션 추가
                .putData("icon", iconUrl) // 데이터 페이로드에 아이콘 추가
                .putData("notification_id", String.valueOf(notificationId))
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
