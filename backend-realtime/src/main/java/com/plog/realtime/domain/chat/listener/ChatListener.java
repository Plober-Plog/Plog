package com.plog.realtime.domain.chat.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plog.realtime.domain.chat.dto.request.ChatGetRequestDto;
import com.plog.realtime.domain.chat.session.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatListener implements MessageListener {

    private final RedisMessageListenerContainer redisContainer;
    private final ObjectMapper objectMapper;
    private final WebSocketSessionManager sessionManager;

    public void subscribeToChatRoom(Long userId, String topicName, String sessionId) {

        // 사용자와 주제명으로 구독 상태 확인 및 추가
        if (sessionManager.isUserAlreadySubscribed(userId, topicName)) {
            log.info("User {} is already subscribed to: {}", userId, topicName);
            return;
        }

        sessionManager.addSubscription(userId, topicName, sessionId);
        ChannelTopic channelTopic = new ChannelTopic(topicName);
        redisContainer.addMessageListener(this, channelTopic);
        log.info(" *** Subscribed user {} to chat room: {}", userId, topicName);
        sessionManager.getSessions(userId, topicName).forEach(session -> {});
    }

    public void unsubscribeFromChatRoom(Long userId, String topicName, String sessionId) {
        sessionManager.removeSubscription(userId, topicName, sessionId);
        log.info(" *** Unsubscribed user {} from chat room: {}", userId, topicName);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageBody = new String(message.getBody(), "UTF-8");
            log.info("수신된 메시지 본문: {}", messageBody);

            String jsonMessage = messageBody.substring(messageBody.indexOf(",") + 1, messageBody.length() - 1);

            ChatGetRequestDto chatGetRequestDto = objectMapper.readValue(jsonMessage, ChatGetRequestDto.class);
            String topicName = "chatroom-" + chatGetRequestDto.getChatRoomId();

            // 세션 관리자에서 메시지를 브로드캐스트
            sessionManager.broadcastMessageToChannel(topicName, chatGetRequestDto);

            log.info("메시지를 전송했습니다: {}", chatGetRequestDto);

        } catch (IOException e) {
            log.error("Failed to process message", e);
        }
    }
}
