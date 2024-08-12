package com.plog.realtime.domain.chat.service;

import com.plog.realtime.domain.chat.dto.request.ChatRoomCreateRequestDto;
import com.plog.realtime.domain.chat.dto.request.ChatRoomUpdateRequestDto;
import com.plog.realtime.domain.chat.dto.response.ChatRoomGetListResponseDto;
import com.plog.realtime.domain.chat.entity.Chat;
import com.plog.realtime.domain.chat.entity.ChatRoom;
import com.plog.realtime.domain.chat.entity.ChatUser;
import com.plog.realtime.domain.chat.repository.*;
import com.plog.realtime.domain.user.entity.User;
import com.plog.realtime.domain.user.repository.UserRepository;
import com.plog.realtime.global.exception.EntityNotFoundException;
import com.plog.realtime.global.exception.NotValidRequestException;
import com.plog.realtime.global.model.response.BaseResponseBody;
import com.plog.realtime.global.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service("chatRoomService")
public class ChatRoomServiceImpl implements ChatRoomService {
    private final JwtTokenUtil jwtTokenUtil;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomRepositorySupport chatRoomRepositorySupport;
    private final ChatUserRepository chatUserRepository;
    private final ChatUserRepositorySupport chatUserRepositorySupport;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    @Transactional
    @Override
    public BaseResponseBody createChatRoom(String token, ChatRoomCreateRequestDto chatRoomCreateRequestDto) {
        log.info(">>> createChatRoom 호출됨");
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> 토큰에서 추출된 userId: {}", userId);

        User targetUser = userRepository.findUserBySearchId(chatRoomCreateRequestDto.getTargetSearchId())
                .orElseThrow(() -> new EntityNotFoundException("일치하는 타겟을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // token user와 targetSearchId user가 포함된 게 이미 있는지 - 1:1 채팅
        boolean isExisted = chatUserRepositorySupport.areBothUsersInSameChatRoom(userId, targetUser.getUserId());
        if (isExisted)
            throw new NotValidRequestException("이미 존재한 채팅방입니다.");

        ChatRoom chatRoom = ChatRoom.builder()
                .user(user)
                .chatRoomName(chatRoomCreateRequestDto.getChatRoomName())
                .chatRoomType(chatRoomCreateRequestDto.getChatRoomType())
                .build();
        chatRoomRepository.save(chatRoom);
        log.info(">>> 채팅방 생성 완료: {}", chatRoom);

        ChatUser chatUser = ChatUser.builder()
                .user(user)
                .chatRoom(chatRoom)
                .build();
        chatUserRepository.save(chatUser);
        chatUserRepository.save(ChatUser.builder()
                .user(targetUser)
                .chatRoom(chatRoom)
                .build());
        log.info(">>> 채팅방의 채팅인원 등록 완료: {}", chatUser);

        return BaseResponseBody.of(200, "" + chatRoom.getChatRoomId());
    }

    @Override
    public List<ChatRoomGetListResponseDto> getAllChatRooms(String token) {
        log.info(">>> getAllChatRooms 호출됨");
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> 토큰에서 추출된 userId: {}", userId);

        // 사용자가 참여한 모든 채팅방을 가져옵니다.
        List<ChatRoom> chatRooms = chatRoomRepositorySupport.findChatRoomsByUserId(userId);

        // 채팅방에 대한 정보를 담을 DTO 리스트를 생성합니다.
        List<ChatRoomGetListResponseDto> response = chatRooms.stream().map(chatRoom -> {
            // 각 채팅방에 참여하고 있는 사용자들을 가져옵니다.
            List<User> users = chatRepository.findUsersByChatRoom(chatRoom);

            // 마지막 메시지를 가져옵니다.
            Chat lastChat = chatRepository.findTopByChatRoomOrderByCreatedAtDesc(chatRoom);

            // 사용자가 마지막 메시지를 읽었는지 여부를 판단합니다.
            ChatUser chatUser = chatUserRepository.findByUserAndChatRoom(userRepository.findById(userId).orElseThrow(), chatRoom)
                    .orElseThrow(() -> new EntityNotFoundException("ChatUser not found"));
            boolean isRead = lastChat.getCreatedAt().isBefore(chatUser.getLastReadAt());

            // DTO를 생성하여 리스트에 추가합니다.
            return new ChatRoomGetListResponseDto(chatRoom, users, lastChat, isRead);
        }).collect(Collectors.toList());

        log.info(">>> 조회된 채팅방 목록: {}", response);

        return response;
    }

    @Transactional
    public void updateLastReadAt(String token, Long chatRoomId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 못 찾았습니다."));
        ChatUser chatUser = chatUserRepository.findByUserAndChatRoom(userRepository.findById(userId).orElseThrow(), chatRoom)
                .orElseThrow(() -> new EntityNotFoundException("채팅 참여자를 못 찾았습니다."));

        chatUser.setLastReadAt(LocalDateTime.now());
        chatUserRepository.save(chatUser);
    }

    @Override
    public List<User> getChatRoomUsers(Long chatRoomId) {
        log.info(">>> getChatRoomUsers 호출됨, chatRoomId: {}", chatRoomId);
        List<User> users = chatRepository.findUsersByChatRoom(chatRoomRepository.findByChatRoomId(chatRoomId));
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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ChatRoom chatRoom = chatRoomRepository.findByChatRoomIdAndUser(chatRoomId, user)
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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ChatRoom chatRoom = chatRoomRepository.findByChatRoomIdAndUser(chatRoomId, user)
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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ChatRoom chatRoom =chatRoomRepository.findByChatRoomId(chatRoomId);

        ChatUser chatUser = chatUserRepository.findByUserAndChatRoom(user, chatRoom)
                .orElseThrow(() -> {
                    log.error(">>> 없는 방, chatRoomId: {}, userId: {}", chatRoomId, userId);
                    return new EntityNotFoundException("없는 방입니다.");
                });

        chatUserRepository.delete(chatUser);
        log.info(">>> 채팅방 나가기 완료: {}", chatUser);

        return BaseResponseBody.of(200, "성공적으로 방에서 나왔습니다.");
    }
}
