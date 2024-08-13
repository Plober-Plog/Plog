package com.plog.realtime.domain.chat.controller;

import com.plog.realtime.domain.chat.dto.request.ChatRoomCreateRequestDto;
import com.plog.realtime.domain.chat.dto.request.ChatRoomUpdateRequestDto;
import com.plog.realtime.domain.chat.dto.response.ChatRoomGetListResponseDto;
import com.plog.realtime.domain.chat.entity.ChatRoom;
import com.plog.realtime.domain.chat.repository.ChatRoomRepository;
import com.plog.realtime.domain.chat.service.ChatRoomService;
import com.plog.realtime.domain.user.entity.User;
import com.plog.realtime.global.exception.NoTokenRequestException;
import com.plog.realtime.global.exception.NotValidRequestException;
import com.plog.realtime.global.model.response.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat/room")
@Tag(name = "ChatRoom", description = "채팅방 API")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    @Operation(summary = "채팅방 개설", description = "새로운 채팅방을 개설합니다.")
    @PostMapping
    public ResponseEntity<?> createChatRoom(
            @RequestHeader("Authorization") String token,
            @RequestBody ChatRoomCreateRequestDto createRequestDto) {
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        log.info(">>> createChatRoom 호출됨");
        if (createRequestDto.getChatRoomName() == null || createRequestDto.getChatRoomName().isEmpty()) {
            log.error(">>> 채팅방 제목이 비어있음");
            throw new NotValidRequestException("채팅방 제목은 필수 입력입니다.");
        }
        if (!(createRequestDto.getChatRoomType() == 1 || createRequestDto.getChatRoomType() == 2)) {
            log.error(">>> 유효하지 않은 방 종류: {}", createRequestDto.getChatRoomType());
            throw new NotValidRequestException("유효하지 않은 방 종류 입니다.");
        }
        log.info(">>> 채팅방 생성 요청: {}", createRequestDto);
        return ResponseEntity.ok(chatRoomService.createChatRoom(token, createRequestDto));
    }

    @Operation(summary = "채팅방 목록 조회", description = "사용자가 소속된 모든 채팅방 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ChatRoomGetListResponseDto>> getAllChatRooms(
            @RequestHeader("Authorization") String token,
            @RequestParam("page") int page) {
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        log.info(">>> getAllChatRooms 호출됨");
        List<ChatRoomGetListResponseDto> chatRoomList = chatRoomService.getAllChatRooms(token, page);
        log.info(">>> 조회된 채팅방 목록: {}", chatRoomList.stream().toList());
        return ResponseEntity.ok(chatRoomList);
    }

    @Operation(summary = "채팅방 입장", description = "사용자가 채팅방에 입장을 합니다.")
    @PostMapping("/{chatRoomId}/read")
    public ResponseEntity<?> updateLastReadAt(@PathVariable Long chatRoomId,
                                              @RequestHeader("Authorization") String token) {
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        chatRoomService.updateLastReadAt(token, chatRoomId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "채팅방 사용자 목록 조회", description = "특정 채팅방에 소속된 사용자 목록을 조회합니다.")
    @GetMapping("/{chatRoomId}/user")
    public ResponseEntity<?> getChatRoomUserList(@PathVariable("chatRoomId") Long chatRoomId) {
        log.info(">>> getChatRoomUserList 호출됨, chatRoomId: {}", chatRoomId);
        List<User> userList = chatRoomService.getChatRoomUsers(chatRoomId);
        log.info(">>> 조회된 채팅방 유저 목록: {}", userList);
        return ResponseEntity.ok(userList);
    }

    @Operation(summary = "채팅방 정보 조회", description = "특정 채팅방의 정보를 조회합니다.")
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<?> getChatRoom(@PathVariable("chatRoomId") Long chatRoomId) {
        log.info(">>> getChatRoom 호출됨, chatRoomId: {}", chatRoomId);
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
        log.info(">>> 조회된 채팅방 정보: {}", chatRoom);
        return ResponseEntity.ok(chatRoom);
    }

    @Operation(summary = "채팅방 삭제", description = "채팅방 개설자가 채팅방을 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<?> deleteChatRoom(
            @RequestHeader("Authorization") String token,
            @RequestParam("chatRoomId") Long chatRoomId) {
        log.info(">>> deleteChatRoom 호출됨, chatRoomId: {}", chatRoomId);
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        return ResponseEntity.ok(chatRoomService.deleteChatRoom(token, chatRoomId));
    }

    @Operation(summary = "채팅방 나가기", description = "사용자가 채팅방에서 나갑니다.")
    @DeleteMapping("/leave")
    public ResponseEntity<?> leaveChatRoom(
            @RequestHeader("Authorization") String token,
            @RequestParam("chatRoomId") Long chatRoomId) {
        log.info(">>> leaveChatRoom 호출됨, chatRoomId: {}", chatRoomId);
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        return ResponseEntity.ok(chatRoomService.leaveChatRoom(token, chatRoomId));
    }

    @Operation(summary = "채팅방 제목 변경", description = "채팅방의 제목을 변경합니다.")
    @PatchMapping
    public ResponseEntity<?> updateChatRoom(
            @RequestHeader("Authorization") String token,
            @RequestBody ChatRoomUpdateRequestDto updateRequestDto) {
        log.info(">>> updateChatRoom 호출됨, chatRoomId: {}, chatRoomName: {}"
                ,updateRequestDto.getChatRoomId()
                ,updateRequestDto.getChatRoomName()
                );
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        return ResponseEntity.ok(chatRoomService.updateChatRoom(token, updateRequestDto));
    }
}
