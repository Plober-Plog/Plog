package com.plog.realtime.global.util;

import com.plog.realtime.domain.chat.dto.request.ChatGetRequestDto;
import com.plog.realtime.domain.chat.dto.response.ChatGetResponseDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

public class RedisMessagePublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    public RedisMessagePublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void publish(ChatGetRequestDto message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
//        redisTemplate.opsForList().rightPush("chatroom", message); // 메시지를 Redis 리스트에 저장
    }
}
