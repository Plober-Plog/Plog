package com.plog.realtime.domain.chat.service;

import com.plog.realtime.domain.chat.dto.request.ChatGetRequestDto;
import com.plog.realtime.domain.chat.dto.response.ChatGetResponseDto;

import java.util.List;

public interface ChatService {
    void addUser(ChatGetRequestDto chatGetRequestDto, String sessionId);
    void sendMessage(ChatGetRequestDto chatGetRequestDto);
    void leaveUser(ChatGetRequestDto chatGetRequestDto, String sessionId);
    List<ChatGetResponseDto> getChatData(Long userId, Long chatRoomId, int page);
}
