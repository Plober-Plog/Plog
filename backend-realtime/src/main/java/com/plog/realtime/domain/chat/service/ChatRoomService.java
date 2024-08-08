package com.plog.realtime.domain.chat.service;

import com.plog.realtime.domain.chat.dto.request.ChatRoomCreateRequestDto;
import com.plog.realtime.domain.chat.entity.ChatRoom;
import com.plog.realtime.global.model.dto.BaseEntity;
import com.plog.realtime.global.model.response.BaseResponseBody;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ChatRoomService {
    public BaseResponseBody createChatRoom(String token, ChatRoomCreateRequestDto chatRoomCreateRequestDto);
    public List<ChatRoom> getAllChatRooms(String token);
    public BaseResponseBody updateChatRoom(String token, Long chatRoomId);
    public BaseResponseBody deleteChatRoom(String token, Long chatRoomId);
}
