package com.plog.realtime.domain.chat.service;

import com.plog.realtime.domain.chat.dto.request.ChatRoomCreateRequestDto;
import com.plog.realtime.domain.chat.dto.request.ChatRoomUpdateRequestDto;
import com.plog.realtime.domain.chat.entity.ChatRoom;
import com.plog.realtime.domain.chat.entity.ChatUser;
import com.plog.realtime.domain.chat.repository.ChatRoomRepositorySupport;
import com.plog.realtime.domain.chat.repository.ChatRoomRepository;
import com.plog.realtime.domain.chat.repository.ChatUserRepository;
import com.plog.realtime.domain.user.entity.User;
import com.plog.realtime.global.exception.EntityNotFoundException;
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
    private final JwtTokenUtil jwtTokenUtil;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomRepositorySupport chatRoomRepositorySupport;
    private final ChatUserRepository chatUserRepository;

    @Transactional
    @Override
    public BaseResponseBody createChatRoom(String token, ChatRoomCreateRequestDto chatRoomCreateRequestDto) {
        log.info(">>> createChatRoom 호출됨");
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> 토큰에서 추출된 userId: {}", userId);

        ChatRoom chatRoom = ChatRoom.builder()
                .userId(userId)
                .chatRoomName(chatRoomCreateRequestDto.getChatRoomName())
                .chatRoomType(chatRoomCreateRequestDto.getChatRoomType())
                .build();
        chatRoomRepository.save(chatRoom);
        log.info(">>> 채팅방 생성 완료: {}", chatRoom);

        return BaseResponseBody.of(200, "성공적으로 방이 만들어졌습니다.");
    }

    @Override
    public List<ChatRoom> getAllChatRooms(String token) {
        log.info(">>> getAllChatRooms 호출됨");
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> 토큰에서 추출된 userId: {}", userId);

        List<ChatRoom> chatRooms = chatRoomRepositorySupport.findByChatUserId(userId);
        log.info(">>> 조회된 채팅방 목록: {}", chatRooms);

        return chatRooms;
    }

    @Override
    public List<User> getChatRoomUsers(Long chatRoomId) {
        log.info(">>> getChatRoomUsers 호출됨, chatRoomId: {}", chatRoomId);
        List<User> users = chatRoomRepository.findUserByChatRoomId(chatRoomId);
        log.info(">>> 조회된 채팅방 유저 목록: {}", users);

        return users;
    }

    @Override
    public ChatRoom getChatRoom(Long chatRoomId) {
        log.info(">>> getChatRoom 호출됨, chatRoomId: {}", chatRoomId);
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId);
        if (chatRoom == null) {
            log.error(">>> 채팅방이 없음, chatRoomId: {}", chatRoomId);
            throw new NotValidRequestException("채팅방이 없습니다.");
        }
        log.info(">>> 조회된 채팅방: {}", chatRoom);

        return chatRoom;
    }

    @Transactional
    @Override
    public BaseResponseBody updateChatRoom(String token, ChatRoomUpdateRequestDto chatRoomUpdateRequestDto) {
        Long chatRoomId = chatRoomUpdateRequestDto.getChatRoomId();
        String chatRoomName = chatRoomUpdateRequestDto.getChatRoomName();
        log.info(">>> updateChatRoom 호출됨, chatRoomId: {}, chatRoomName: {}", chatRoomId, chatRoomName);
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> 토큰에서 추출된 userId: {}", userId);

        ChatRoom chatRoom = chatRoomRepository.findByChatRoomIdAndUserId(chatRoomId, userId)
                .orElseThrow(() -> {
                    log.error(">>> 권한이 없거나, 없는 방, chatRoomId: {}, userId: {}", chatRoomId, userId);
                    return new EntityNotFoundException("권한이 없거나, 없는 방입니다.");
                });

        chatRoom.setChatRoomName(chatRoomName);
        chatRoomRepository.save(chatRoom);
        log.info(">>> 채팅방 이름 변경 완료: {}", chatRoom);

        return BaseResponseBody.of(200, "성공적으로 방 이름을 바꿨습니다. : " + chatRoom.getChatRoomName());
    }

    @Transactional
    @Override
    public BaseResponseBody deleteChatRoom(String token, Long chatRoomId) {
        log.info(">>> deleteChatRoom 호출됨, chatRoomId: {}", chatRoomId);
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> 토큰에서 추출된 userId: {}", userId);

        ChatRoom chatRoom = chatRoomRepository.findByChatRoomIdAndUserId(chatRoomId, userId)
                .orElseThrow(() -> {
                    log.error(">>> 권한이 없거나, 없는 방, chatRoomId: {}, userId: {}", chatRoomId, userId);
                    return new EntityNotFoundException("권한이 없거나, 없는 방입니다.");
                });

        chatRoom.setDeleted(true);
        chatRoomRepository.save(chatRoom);
        log.info(">>> 채팅방 삭제 완료: {}", chatRoom);

        return BaseResponseBody.of(200, "방을 성공적으로 삭제했습니다.");
    }

    @Transactional
    @Override
    public BaseResponseBody leaveChatRoom(String token, Long chatRoomId) {
        log.info(">>> leaveChatRoom 호출됨, chatRoomId: {}", chatRoomId);
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> 토큰에서 추출된 userId: {}", userId);

        ChatUser chatUser = chatUserRepository.findByUserIdAndChatRoomId(userId, chatRoomId)
                .orElseThrow(() -> {
                    log.error(">>> 없는 방, chatRoomId: {}, userId: {}", chatRoomId, userId);
                    return new EntityNotFoundException("없는 방입니다.");
                });

        chatUserRepository.delete(chatUser);
        log.info(">>> 채팅방 나가기 완료: {}", chatUser);

        return BaseResponseBody.of(200, "성공적으로 방에서 나왔습니다.");
    }
}
