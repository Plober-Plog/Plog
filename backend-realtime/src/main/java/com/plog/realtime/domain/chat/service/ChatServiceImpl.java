package com.plog.realtime.domain.chat.service;

import com.plog.realtime.domain.chat.dto.request.ChatGetRequestDto;
import com.plog.realtime.domain.chat.dto.response.ChatGetResponseDto;
import com.plog.realtime.domain.chat.entity.Chat;
import com.plog.realtime.domain.chat.entity.ChatUser;
import com.plog.realtime.domain.chat.listener.ChatListener;
import com.plog.realtime.domain.chat.repository.ChatRepository;
import com.plog.realtime.domain.chat.repository.ChatRepositorySupport;
import com.plog.realtime.domain.chat.repository.ChatRoomRepository;
import com.plog.realtime.domain.chat.repository.ChatUserRepository;
import com.plog.realtime.domain.user.repository.UserRepository;
import com.plog.realtime.global.exception.EntityNotFoundException;
import com.plog.realtime.global.exception.NotAuthorizedRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service("chatService")
public class ChatServiceImpl implements ChatService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatListener chatListener;

    private final ChatUserRepository chatUserRepository;
    private final ChatRepository chatRepository;
    private final ChatRepositorySupport chatRepositorySupport;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;


    @Override
    public void addUser(ChatGetRequestDto chatGetRequestDto, String sessionId) {
        ChatUser chatUser = chatUserRepository.findByUserUserIdAndChatRoomChatRoomId(chatGetRequestDto.getUserId(), chatGetRequestDto.getChatRoomId())
                .orElseThrow(() -> new NotAuthorizedRequestException("채팅창에 입장할 권한이 없습니다."));
        Long userId = chatGetRequestDto.getUserId();
        String topicName = "chatroom-" + chatGetRequestDto.getChatRoomId();
        chatListener.subscribeToChatRoom(userId, topicName, sessionId);
        log.info(" >>> channel 구독 완료 : {}", topicName);
        redisTemplate.convertAndSend(topicName, chatGetRequestDto);
        log.info(" >>> addUser 완료 : {}", chatGetRequestDto.getUserId());
    }

    @Transactional
    @Override
    public void sendMessage(ChatGetRequestDto chatGetRequestDto) {
        chatRepository.save(
                Chat.builder()
                        .user(userRepository.getReferenceById(chatGetRequestDto.getUserId()))
                        .chatRoom(chatRoomRepository.getReferenceById(chatGetRequestDto.getChatRoomId()))
                        .message(chatGetRequestDto.getMessage())
                        .build()
        );
        ChatUser chatUser = chatUserRepository.findFirstByUserAndChatRoom(
                userRepository.getReferenceById(chatGetRequestDto.getUserId()),
                chatRoomRepository.getReferenceById(chatGetRequestDto.getChatRoomId())
        ).orElseThrow(()-> {
            return new EntityNotFoundException("찾을 수 없는 유저이거나 채팅방 입니다.");
        });
        chatUser.setLastReadAt(LocalDateTime.now());
        chatUserRepository.save(chatUser);

        log.info(" >>> sendMessage 완료 - DB에 저장: {}", chatGetRequestDto.getUserId());
        String topicName = "chatroom-" + chatGetRequestDto.getChatRoomId();
        redisTemplate.convertAndSend(topicName, chatGetRequestDto);
    }

    @Override
    public void leaveUser(ChatGetRequestDto chatGetRequestDto, String sessionId) {
        String topicName = "chatroom-" + chatGetRequestDto.getChatRoomId();
        redisTemplate.convertAndSend(topicName, chatGetRequestDto);
        chatListener.unsubscribeFromChatRoom(chatGetRequestDto.getUserId(), topicName, sessionId);
        log.info(" >>> leaveUser 완료: {}", chatGetRequestDto.getUserId());
    }

    @Override
    public List<ChatGetResponseDto> getChatData(Long userId, Long chatRoomId, int page) {
        ChatUser chatUser = chatUserRepository.findByUserUserIdAndChatRoomChatRoomId(userId, chatRoomId)
                .orElseThrow(() -> new NotAuthorizedRequestException("채팅창에 입장할 권한이 없습니다."));

        List<Chat> chats = chatRepositorySupport.findChatsByChatRoomId(chatRoomId, page);
        for(Chat chat : chats) {
            log.info(">>> getChatDate - chat : {}", chat.toString());
        }
        log.info(" >>> getChatData 완료: chatRoomId - {}, page - {}", chatRoomId, page);
        return chats.stream().map(chat -> new ChatGetResponseDto(
                chat.getUser().getUserId(),
                chat.getChatRoom().getChatRoomId(),
                chat.getUser().getNickname(),
                chat.getUser().getSearchId(),
                chat.getUser().getImage().getImageUrl(),
                chat.getMessage(),
                chat.getCreatedAt()
        )).collect(Collectors.toList());
    }
}
