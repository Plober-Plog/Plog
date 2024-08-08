package com.plog.realtime.domain.chat.service;

import com.plog.realtime.domain.chat.dto.request.ChatGetRequestDto;
import com.plog.realtime.domain.chat.dto.response.ChatGetResponseDto;
import com.plog.realtime.domain.chat.entity.Chat;
import com.plog.realtime.domain.chat.entity.ChatUser;
import com.plog.realtime.domain.chat.repository.ChatRepository;
import com.plog.realtime.domain.chat.repository.ChatRepositorySupport;
import com.plog.realtime.domain.chat.repository.ChatRoomRepository;
import com.plog.realtime.domain.chat.repository.ChatUserRepository;
import com.plog.realtime.domain.user.repository.UserRepository;
import com.plog.realtime.global.exception.NotAuthorizedRequestException;
import com.plog.realtime.global.util.RedisMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service("chatService")
public class ChatServiceImpl implements ChatService {

    private final RedisMessagePublisher redisMessagePublisher;
    private final ChatUserRepository chatUserRepository;
    private final ChatRepository chatRepository;
    private final ChatRepositorySupport chatRepositorySupport;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;


    @Override
    public void addUser(ChatGetRequestDto chatGetRequestDto) {
        ChatUser chatUser = chatUserRepository.findByUserIdAndChatRoomId(chatGetRequestDto.getUserId(), chatGetRequestDto.getChatRoomId())
                .orElseThrow(() -> new NotAuthorizedRequestException("채팅창에 입장할 권한이 없습니다."));
        redisMessagePublisher.publish(chatGetRequestDto);
        log.info(" >>> addUser 완료 : {}", chatGetRequestDto.getUserId());
    }

    @Override
    public void sendMessage(ChatGetRequestDto chatGetRequestDto) {
        chatRepository.save(
                Chat.builder()
                        .user(userRepository.getReferenceById(chatGetRequestDto.getUserId()))
                        .chatRoom(chatRoomRepository.getReferenceById(chatGetRequestDto.getChatRoomId()))
                        .message(chatGetRequestDto.getMessage())
                        .build()
        );
        log.info(" >>> sendMessage 완료 - DB에 저장: {}", chatGetRequestDto.getUserId());
        redisMessagePublisher.publish(chatGetRequestDto);
    }

    @Override
    public void leaveUser(ChatGetRequestDto chatGetRequestDto) {
        log.info(" >>> leaveUser 완료: {}", chatGetRequestDto.getUserId());
        redisMessagePublisher.publish(chatGetRequestDto);
    }

    @Override
    public List<ChatGetResponseDto> getChatData(Long userId, Long chatRoomId, int page) {
        ChatUser chatUser = chatUserRepository.findByUserIdAndChatRoomId(userId, chatRoomId)
                .orElseThrow(() -> new NotAuthorizedRequestException("채팅창에 입장할 권한이 없습니다."));

        List<Chat> chats = chatRepositorySupport.findChatsByChatRoomId(chatRoomId, page);
        log.info(" >>> getChatData 완료: chatRoomId - {}, page - {}", chatRoomId, page);
        return chats.stream().map(chat -> new ChatGetResponseDto(
                chat.getUser().getUserId(),
                chat.getUser().getNickname(),
                chat.getUser().getImage().getImageUrl(),
                chat.getMessage(),
                chat.getCreatedAt()
        )).collect(Collectors.toList());
    }
}
