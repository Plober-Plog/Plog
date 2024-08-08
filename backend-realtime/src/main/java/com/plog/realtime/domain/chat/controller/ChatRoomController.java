package com.plog.realtime.domain.chat.controller;

import com.plog.realtime.domain.chat.dto.request.ChatRoomCreateRequestDto;
import com.plog.realtime.domain.chat.entity.ChatRoom;
import com.plog.realtime.domain.chat.service.ChatRoomService;
import com.plog.realtime.global.exception.NotValidRequestException;
import com.plog.realtime.global.model.dto.BaseEntity;
import com.plog.realtime.global.model.response.BaseResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/chat/room")
public class ChatRoomController {
    private static ChatRoomService chatRoomService;

    // post -> 채팅방 개설
    @PostMapping
    public ResponseEntity<?> createChatRoom(
            @RequestHeader("Authorization") String token,
            @RequestBody ChatRoomCreateRequestDto createRequestDto){
        if(createRequestDto.getChatRoomName() == null || createRequestDto.getChatRoomName().isEmpty()){
            throw new NotValidRequestException("채팅방 제목은 필수 입력입니다.");
        }
        if(!(createRequestDto.getType() == 1 || createRequestDto.getType() == 2)) {
            throw new NotValidRequestException("유효하지 않은 방 종류 입니다.");
        }
        return ResponseEntity.ok(chatRoomService.createChatRoom(token, createRequestDto));
    }

    // get -> 내가 소속되어 있는 채팅방 리스트 조회
    @GetMapping
    public ResponseEntity<?> getAllChatRooms(@RequestHeader("Authorization") String token) {
        List<ChatRoom> chatRoomList = chatRoomService.getAllChatRooms(token);
        return ResponseEntity.ok(chatRoomList);
    }

    // get -> 해당 채팅방에 소속되어 있는 사람

    // get -> 해당 채팅방 자체 정보

    // delete -> 개설자는 채팅방 삭제 (들어와있는 사람은 접근할 수 없음)

    // delete -> 그냥 그 채팅방 나가기

    // patch -> 제목 바꾸기
}
