package com.plog.backend.domain.neighbor.controller;

import com.plog.backend.domain.neighbor.dto.NeighborAddRequestDto;
import com.plog.backend.domain.neighbor.service.NeighborServiceImpl;
import com.plog.backend.global.model.response.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/neighbor")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User API", description = "User 관련 API")
public class NeighborController {

    private final NeighborServiceImpl neighborService;

    // 이웃 추가
    @Operation(summary = "이웃 추가", description = "회원 가입을 처리합니다.")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestHeader("Authorization") String token, @RequestBody NeighborAddRequestDto neighborAddRequestDto) {
        neighborService.addNeighbor(token, neighborAddRequestDto.getNeighborId());
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "이웃이 추가 되었습니다."));
    }
}