package com.plog.realtime.domain.chat.controller;

import com.plog.realtime.domain.chat.dto.request.ChatGetRequestDto;
import com.plog.realtime.domain.chat.dto.response.ChatGetResponseDto;
import com.plog.realtime.domain.chat.service.ChatService;
import com.plog.realtime.global.exception.NotValidRequestException;
import com.plog.realtime.global.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final JwtTokenUtil jwtTokenUtil;

    @MessageMapping("/chat.sendMessage/{chatRoomId}")
    public void sendMessage(
            @DestinationVariable("chatRoomId") String chatRoomId,
            ChatGetRequestDto chatGetRequestDto,
            StompHeaderAccessor headerAccessor
    ) {
        String token = headerAccessor.getFirstNativeHeader("Authorization");
        if (token == null) {
            throw new NotValidRequestException("토큰은 필수값입니다.");
        }
        Long userId = JwtTokenUtil.jwtTokenUtil.getUserIdFromToken(token);
        chatGetRequestDto.setUserId(userId);
        chatGetRequestDto.setChatRoomId(Long.parseLong(chatRoomId));
        log.info(" >>> sendMessage: " + chatGetRequestDto);
        chatService.sendMessage(chatGetRequestDto);
    }

    @MessageMapping("/chat.addUser/{chatRoomId}")
    public void addUser(
            @DestinationVariable("chatRoomId") String chatRoomId,
            ChatGetRequestDto chatGetRequestDto,
            StompHeaderAccessor headerAccessor
    ) {
        String token = headerAccessor.getFirstNativeHeader("Authorization");
        if (token == null) {
            throw new NotValidRequestException("토큰은 필수값입니다.");
        }
        Long userId = JwtTokenUtil.jwtTokenUtil.getUserIdFromToken(token);
        chatGetRequestDto.setUserId(userId);
        chatGetRequestDto.setChatRoomId(Long.parseLong(chatRoomId));
        log.info(" >>> addUser: " + chatGetRequestDto);
        chatService.addUser(chatGetRequestDto, headerAccessor.getSessionId());
    }

    @MessageMapping("/chat.leaveUser/{chatRoomId}")
    public void leaveUser(
            @DestinationVariable("chatRoomId") String chatRoomId,
            ChatGetRequestDto chatGetRequestDto,
            StompHeaderAccessor headerAccessor
    ) {
        String token = headerAccessor.getFirstNativeHeader("Authorization");
        if (token == null) {
            throw new NotValidRequestException("토큰은 필수값입니다.");
        }
        Long userId = JwtTokenUtil.jwtTokenUtil.getUserIdFromToken(token);
        chatGetRequestDto.setUserId(userId);
        chatGetRequestDto.setChatRoomId(Long.parseLong(chatRoomId));
        log.info(" >>> leaveUser: " + chatGetRequestDto);
        chatService.leaveUser(chatGetRequestDto, headerAccessor.getSessionId());
    }

    @GetMapping("/{chatRoomId}/history")
    public ResponseEntity<List<ChatGetResponseDto>> getChatData(
            @Parameter(description = "인증 토큰")  @RequestHeader("Authorization") String token,
            @Parameter(description = "채팅방 번호")  @PathVariable("chatRoomId") Long chatRoomId,
            @Parameter(description = "page 번호") @RequestParam(required = false, defaultValue = "0") int page
    ) {
        if (token == null) {
            throw new NotValidRequestException("토큰은 필수값입니다.");
        }
        log.info(" >>> getChatData: " + chatRoomId);
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        List<ChatGetResponseDto> chatGetResponseDtoList = chatService.getChatData(userId, chatRoomId, page);
        return ResponseEntity.status(200).body(chatGetResponseDtoList);
    }
}
