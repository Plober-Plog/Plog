package com.plog.realtime.domain.notification.service;

public interface FCMService {
    void sendNotification(String token, String message);
}
