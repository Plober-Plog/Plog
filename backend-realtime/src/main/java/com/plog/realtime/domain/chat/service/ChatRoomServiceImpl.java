package com.plog.realtime.domain.chat.service;

import com.plog.realtime.domain.chat.dto.request.ChatRoomCreateRequestDto;
import com.plog.realtime.domain.chat.entity.ChatRoom;
import com.plog.realtime.domain.chat.repository.ChatRoomRepositorySupport;
import com.plog.realtime.domain.chat.repository.ChatRoomRepository;
import com.plog.realtime.domain.user.entity.User;
import com.plog.realtime.global.exception.NotValidRequestException;
import com.plog.realtime.global.model.response.BaseResponseBody;
import com.plog.realtime.global.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("ChatRoomService")
public class ChatRoomServiceImpl implements ChatRoomService {
    private static JwtTokenUtil jwtTokenUtil;
    private static ChatRoomRepository chatRoomRepository;
    private static ChatRoomRepositorySupport chatRoomRepositorySupport;

    @Transactional
    @Override
    public BaseResponseBody createChatRoom(String token, ChatRoomCreateRequestDto chatRoomCreateRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        ChatRoom chatRoom = ChatRoom.builder()
                .userId(userId)
                .chatRoomName(chatRoomCreateRequestDto.getChatRoomName())
                .chatRoomType(chatRoomCreateRequestDto.getType())
                .build();
        chatRoomRepository.save(chatRoom);

        return BaseResponseBody.of(200, "성공적으로 방이 만들어졌습니다.");
    }

    @Override
    public List<ChatRoom> getAllChatRooms(String token) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        return chatRoomRepositorySupport.findByChatUserId(userId);
    }

    @Override
    public List<User> getChatRoomUsers(Long chatRoomId) {
        return chatRoomRepository.findUserByChatRoomId(chatRoomId);
    }

    @Override
    public ChatRoom getChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId);
        if (chatRoom == null) {
            throw new NotValidRequestException("채팅방이 없습니다.");
        }
        return chatRoom;
    }

    @Override
    public BaseResponseBody updateChatRoom(String token, Long chatRoomId) {
        return null;
    }

    @Override
    public BaseResponseBody deleteChatRoom(String token, Long chatRoomId) {
        return null;
    }
}
