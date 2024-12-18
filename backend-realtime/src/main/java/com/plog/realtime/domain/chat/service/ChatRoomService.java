package com.plog.realtime.domain.chat.service;

import com.plog.realtime.domain.chat.dto.request.ChatRoomCreateRequestDto;
import com.plog.realtime.domain.chat.dto.request.ChatRoomUpdateRequestDto;
import com.plog.realtime.domain.chat.dto.response.ChatRoomGetListResponseDto;
import com.plog.realtime.domain.chat.entity.ChatRoom;
import com.plog.realtime.domain.user.entity.User;
import com.plog.realtime.global.model.dto.BaseEntity;
import com.plog.realtime.global.model.response.BaseResponseBody;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ChatRoomService {
    public BaseResponseBody createChatRoom(String token, ChatRoomCreateRequestDto chatRoomCreateRequestDto);
    public List<ChatRoomGetListResponseDto> getAllChatRooms(String token, int page);
    public List<User> getChatRoomUsers(Long chatRoomId);
    public ChatRoom getChatRoom(Long chatRoomId);
    public BaseResponseBody updateChatRoom(String token, ChatRoomUpdateRequestDto chatRoomUpdateRequestDto);
    public BaseResponseBody deleteChatRoom(String token, Long chatRoomId);
    public BaseResponseBody leaveChatRoom(String token, Long chatRoomId);
    public void updateLastReadAt(String token, Long chatRoomId);
}
