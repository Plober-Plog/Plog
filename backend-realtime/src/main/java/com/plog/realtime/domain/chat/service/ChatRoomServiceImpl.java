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
import com.plog.realtime.global.exception.DuplicateEntityException;
import com.plog.realtime.global.exception.EntityNotFoundException;
import com.plog.realtime.global.exception.NotValidRequestException;
import com.plog.realtime.global.model.response.BaseResponseBody;
import com.plog.realtime.global.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
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
    private final ChatRepositorySupport chatRepositorySupport;

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
        Optional<ChatRoom> isExistedChatroom = chatUserRepositorySupport.findCommonChatRooms(userId, targetUser.getUserId());
        if (isExistedChatroom.isPresent()) {
            // 이미 존재하면 채팅방 번호 넘기기
            throw new DuplicateEntityException(isExistedChatroom.get().getChatRoomId().toString());
        }

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

        chatRepository.save(Chat.builder()
                        .user(user)
                        .chatRoom(chatRoom)
                        .message("채팅방이 개설되었습니다.")
                .build());
        log.info(">>> 채팅방의 채팅인원 등록 완료: {} 번 방 {} 번 회원", chatUser.getChatRoom(), chatUser.getChatUserId());

        return BaseResponseBody.of(200, "" + chatRoom.getChatRoomId());
    }

    @Override
    public List<ChatRoomGetListResponseDto> getAllChatRooms(String token, int page) {
        int pageSize = 15;
        log.info(">>> getAllChatRooms 호출됨");

        // 토큰에서 userId 추출
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> 토큰에서 추출된 userId: {}", userId);

        // 사용자가 참여한 모든 채팅방을 가져옴
        log.info(">>> 사용자가 참여한 채팅방 목록 조회 시작");
        List<ChatRoom> chatRooms = chatRoomRepositorySupport.findChatRoomsByUserId(userId);
        log.info(">>> 조회된 채팅방 수: {}", chatRooms.size());

        // 각 채팅방의 최신 채팅을 기준으로 정렬
        log.info(">>> 각 채팅방의 최신 채팅을 기준으로 정렬 시작");
        List<ChatRoom> sortedChatRooms = chatRooms.stream()
                .sorted((chatRoom1, chatRoom2) -> {
                    Chat lastChat1 = chatRepository.findTopByChatRoomOrderByCreatedAtDesc(chatRoom1);
                    Chat lastChat2 = chatRepository.findTopByChatRoomOrderByCreatedAtDesc(chatRoom2);

                    if (lastChat1 == null && lastChat2 == null) return 0;
                    if (lastChat1 == null) return 1;
                    if (lastChat2 == null) return -1;

                    return lastChat2.getCreatedAt().compareTo(lastChat1.getCreatedAt());
                })
                .collect(Collectors.toList());
        log.info(">>> 정렬 완료");

        // 페이지네이션 적용
        int skipCount = page * pageSize;
        List<ChatRoom> paginatedChatRooms = sortedChatRooms.stream()
                .skip(skipCount)
                .limit(pageSize)
                .collect(Collectors.toList());
        log.info(">>> 페이지네이션 적용 완료 - 페이지: {}, 페이지 크기: {}", page, pageSize);

        // 채팅방에 대한 정보를 담을 DTO 리스트를 생성
        log.info(">>> 채팅방 정보를 DTO로 변환 시작");
        List<ChatRoomGetListResponseDto> response = paginatedChatRooms.stream().map(chatRoom -> {
            log.info(">>> 채팅방 처리 시작 - ChatRoomId: {}", chatRoom.getChatRoomId());

            // 각 채팅방에 참여하고 있는 사용자들을 가져옴
            log.info(">>> 해당 채팅방에 참여한 사용자 조회 시작 - ChatRoomId: {}", chatRoom.getChatRoomId());
            List<User> users = chatUserRepositorySupport.findUsersByChatRoomId(chatRoom.getChatRoomId());
            for(User user : users) {
                log.info(">>> 해당 채팅방에 참여한 사용자 확인 - {}", user.getNickname());
            }
            // 본인은 제외
            users.removeIf(user -> user.getUserId().equals(userId));
            log.info(">>> 조회된 사용자 수: {}", users.size());

            // 마지막 메시지를 가져옴
            log.info(">>> 마지막 메시지 조회 시작 - ChatRoomId: {}", chatRoom.getChatRoomId());
            Chat lastChat = chatRepository.findTopByChatRoomOrderByCreatedAtDesc(chatRoom);
            log.info(">>> 마지막 메시지 조회 완료 - Message: {}", lastChat != null ? lastChat.getMessage() : "메시지가 없습니다.");
            if (lastChat != null) {
//                lastChat.getCreatedAt().atZone(ZoneId.of("Asia/Seoul"));
                lastChat.setCreatedAt(lastChat.getCreatedAt().atZone(ZoneOffset.UTC).toLocalDateTime());
            }

            // 사용자가 마지막 메시지를 읽었는지 여부를 판단
            log.info(">>> 사용자의 메시지 읽음 여부 판단 시작 - ChatRoomId: {}", chatRoom.getChatRoomId());
            ChatUser chatUser = chatUserRepository.findFirstByUserAndChatRoom(userRepository.findById(userId).orElseThrow(), chatRoom)
                    .orElseThrow(() -> new EntityNotFoundException("ChatUser not found"));

            boolean isRead = false;
            if (lastChat != null && chatUser.getLastReadAt() != null) {
                chatUser.setLastReadAt(chatUser.getLastReadAt().atZone(ZoneOffset.UTC).toLocalDateTime());
                log.info(">>> 채팅방의 마지막 메시지와 사용자의 읽은 시간 비교 - lastChat {}, chatUser {}", lastChat.getCreatedAt(), chatUser.getLastReadAt());
                isRead = !lastChat.getCreatedAt().isAfter(chatUser.getLastReadAt());
            }
            log.info(">>> 메시지 읽음 여부: {}", isRead);

            // DTO를 생성하여 리스트에 추가
            log.info(">>> ChatRoomGetListResponseDto 생성 시작 - ChatRoomId: {}", chatRoom.getChatRoomId());
            ChatRoomGetListResponseDto dto = new ChatRoomGetListResponseDto(chatRoom, users, lastChat, isRead);
            log.info(">>> ChatRoomGetListResponseDto 생성 완료 - DTO: {}", dto);

            return dto;
        }).collect(Collectors.toList());

        log.info(">>> 조회된 채팅방 목록 최종 반환 - 총 {}개의 채팅방", response.size());

        return response;
    }

    @Transactional
    public void updateLastReadAt(String token, Long chatRoomId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 못 찾았습니다."));
        ChatUser chatUser = chatUserRepository.findFirstByUserAndChatRoom(userRepository.findById(userId).orElseThrow(), chatRoom)
                .orElseThrow(() -> new EntityNotFoundException("채팅 참여자를 못 찾았습니다."));

//        Chat lastChat = chatRepository.findTopByChatRoomOrderByCreatedAtDesc(chatRoom);
//
//        if(lastChat == null) {
//            log.info(">>> 최근 채팅이 없음 - 메소드 종료됨");
//            lastChat = new Chat();
//            lastChat.setChatRoom(chatRoom);
//            lastChat.setUser(userRepository.findByUserId(userId));
//            lastChat.setMessage("채팅이 시작 되었습니다.");
//            chatRepository.save(lastChat);
//        }
//
//        // 그 채팅이 현재 사용자가 작성한 것이 아니라면 메소드 종료
//        if (!lastChat.getUser().getUserId().equals(userId)) {
//            log.info(">>> 최근 채팅이 사용자가 작성한 것이 아님 - 메소드 종료됨");
//            return;
//        }

        chatUser.setLastReadAt(LocalDateTime.now());
        chatUserRepository.save(chatUser);
    }

    @Override
    public List<User> getChatRoomUsers(Long chatRoomId) {
        log.info(">>> getChatRoomUsers 호출됨, chatRoomId: {}", chatRoomId);
        List<User> users = chatUserRepositorySupport.findUsersByChatRoomId(chatRoomId);
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

        ChatUser chatUser = chatUserRepository.findFirstByUserAndChatRoom(user, chatRoom)
                .orElseThrow(() -> {
                    log.error(">>> 없는 방, chatRoomId: {}, userId: {}", chatRoomId, userId);
                    return new EntityNotFoundException("없는 방입니다.");
                });

        chatUserRepository.delete(chatUser);
        log.info(">>> 채팅방 나가기 완료: {}", chatUser);

        return BaseResponseBody.of(200, "성공적으로 방에서 나왔습니다.");
    }
}
