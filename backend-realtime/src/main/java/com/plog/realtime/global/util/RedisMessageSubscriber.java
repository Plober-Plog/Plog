package com.plog.realtime.global.util;

import org.springframework.messaging.simp.SimpMessagingTemplate;

public class RedisMessageSubscriber {

    private final SimpMessagingTemplate messagingTemplate;

    public RedisMessageSubscriber(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void onMessage(String message, String channel) {
        System.out.println("Received Message: " + message + " from Channel: " + channel);
        messagingTemplate.convertAndSend("/topic/public", message);
    }
}
