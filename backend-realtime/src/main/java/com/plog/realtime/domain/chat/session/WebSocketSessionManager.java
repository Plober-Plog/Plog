package com.plog.realtime.domain.chat.session;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketSessionManager {
    // 채널 ID를 키로 하고, 각 채널에 구독된 세션과 사용자 ID를 함께 관리
    private final Map<String, Map<Long, Set<String>>> channelSubscriptions = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate messagingTemplate;

    // 채널 구독 추가
    public boolean addSubscription(Long userId, String channelId, String sessionId) {
        Map<Long, Set<String>> userSubscriptions = channelSubscriptions.computeIfAbsent(channelId, k -> new ConcurrentHashMap<>());
        Set<String> sessions = userSubscriptions.computeIfAbsent(userId, k -> new CopyOnWriteArraySet<>());

        return sessions.add(sessionId);  // 이미 구독 중인 경우 false 반환
    }

    // 채널 구독 제거
    public void removeSubscription(Long userId, String channelId, String sessionId) {
        Map<Long, Set<String>> userSubscriptions = channelSubscriptions.get(channelId);
        if (userSubscriptions != null) {
            Set<String> sessions = userSubscriptions.get(userId);
            if (sessions != null) {
                sessions.remove(sessionId);
                if (sessions.isEmpty()) {
                    userSubscriptions.remove(userId);
                    if (userSubscriptions.isEmpty()) {
                        channelSubscriptions.remove(channelId);
                    }
                }
            }
        }
    }

    // 특정 채널에 구독된 모든 세션 가져오기
    public Set<String> getSessions(Long userId, String channelId) {
        Map<Long, Set<String>> userSubscriptions = channelSubscriptions.getOrDefault(channelId, new ConcurrentHashMap<>());
        log.info("해당 채널에 구독한 모든 세션 정보 출력: {}", userSubscriptions.toString());
        return userSubscriptions.getOrDefault(userId, new CopyOnWriteArraySet<>());
    }

    // 특정 채널에 사용자가 이미 구독 중인지 확인
    public boolean isUserAlreadySubscribed(Long userId, String channelId) {
        Map<Long, Set<String>> userSubscriptions = channelSubscriptions.get(channelId);
        return userSubscriptions != null && userSubscriptions.containsKey(userId);
    }

    public void broadcastMessageToChannel(String channelId, Object message) {
        Map<Long, Set<String>> userSubscriptions = channelSubscriptions.get(channelId);
        if (userSubscriptions != null) {
            userSubscriptions.forEach((userId, sessions) -> sessions.forEach(sessionId -> {
                // 세션에 메시지를 전송
                messagingTemplate.convertAndSend("/topic/" + channelId, message);
            }));
        }
    }
}
