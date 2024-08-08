package com.plog.realtime.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plog.realtime.domain.chat.dto.request.ChatGetRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;

@RequiredArgsConstructor
@Service("chatService")
public class ChatServiceImpl implements ChatService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;


    public void publishMessage(String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
    }

    public void saveMessage(ChatGetRequestDto chatMessage) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(chatMessage);
            redisTemplate.opsForList().rightPush("chatMessages", jsonMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // 예외 처리 로직을 추가할 수 있습니다.
        }
    }
}
