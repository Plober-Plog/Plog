package com.plog.realtime.domain.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plog.realtime.domain.chat.dto.response.ChatGetResponseDto;
import com.plog.realtime.global.util.RedisMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/chat")
public class ChatController {
    private final RedisMessagePublisher redisMessagePublisher;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatGetResponseDto chatMessage) throws JsonProcessingException {
        log.info(" >>> sendMessage: " + chatMessage);
        chatMessage.setCreatedAt(LocalDateTime.now());
        String jsonMessage = objectMapper.writeValueAsString(chatMessage);
        redisMessagePublisher.publish(chatMessage);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(ChatGetResponseDto chatMessage) {
        log.info(" >>> addUser: " + chatMessage);
        chatMessage.setMessage(chatMessage.getNickname() + " joined");
        chatMessage.setCreatedAt(LocalDateTime.now());
        redisMessagePublisher.publish(chatMessage);
    }

    @MessageMapping("/chat.leaveUser")
    public void leaveUser(ChatGetResponseDto chatMessage) {
        log.info(" >>> leaveUser: " + chatMessage);
        chatMessage.setMessage(chatMessage.getNickname() + " leaved");
        chatMessage.setCreatedAt(LocalDateTime.now());
        redisMessagePublisher.publish(chatMessage);
    }

//    // Redis에 저장된 메시지를 확인하는 엔드포인트
//    @GetMapping("/chat/messages")
//    public List<String> getMessages() {
//        return redisTemplate.opsForList().range("chatroom", 0, -1);
//    }
}

