package com.plog.realtime.domain.chat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user/chat-room")
public class ChatRoomController {
    // post -> 채팅방 개설

    // get -> 내가 소속되어 있는 채팅방 리스트 조회

    // get -> 해당 채팅방에 소속되어 있는 사람

    // get -> 해당 채팅방 자체 정보

    // delete -> 개설자는 채팅방 삭제 (들어와있는 사람은 접근할 수 없음)

    // delete -> 그냥 그 채팅방 나가기

    // patch -> 제목 바꾸기
}
