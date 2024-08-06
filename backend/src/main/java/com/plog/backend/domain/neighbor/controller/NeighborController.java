package com.plog.backend.domain.neighbor.controller;

import com.plog.backend.domain.neighbor.dto.NeighborAddRequestDto;
import com.plog.backend.domain.neighbor.dto.NeighborMutualAddRequestDto;
import com.plog.backend.domain.neighbor.service.NeighborServiceImpl;
import com.plog.backend.global.exception.NotValidRequestException;
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
@Tag(name = "Neighbor API", description = "이웃 관련 API")
public class NeighborController {

    private final NeighborServiceImpl neighborService;

    // 이웃 추가
    @Operation(summary = "이웃 추가", description = "이웃을 추가합니다.")
    @PostMapping
    public ResponseEntity<?> createNeighbor(@RequestHeader("Authorization") String token, @RequestBody NeighborAddRequestDto neighborAddRequestDto) {
        neighborService.addNeighbor(token, neighborAddRequestDto.getNeighborSearchId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "이웃이 추가 되었습니다."));
    }

    // 이웃 삭제
    @Operation(summary = "이웃 삭제", description = "이웃을 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<?> deleteNeighbor(@RequestHeader("Authorization") String token, @RequestBody NeighborAddRequestDto neighborAddRequestDto) {
        neighborService.deleteNeighbor(token, neighborAddRequestDto.getNeighborSearchId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "이웃이 삭제 되었습니다."));
    }

    // 서로 이웃 추가
    @Operation(summary = "서로 이웃 추가", description = "이웃을 추가합니다.")
    @PostMapping("/mutual/access")
    public ResponseEntity<?> createMutualNeighbor(@RequestHeader("Authorization") String token, @RequestBody NeighborMutualAddRequestDto neighborAddRequestDto) {
        neighborService.addMutualNeighbor(token, neighborAddRequestDto.getNeighborSearchId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "서로 이웃이 추가 되었습니다."));
    }

    // 서로 이웃 삭제
    @Operation(summary = "서로 이웃 삭제", description = "이웃을 삭제합니다.")
    @DeleteMapping("/mutual")
    public ResponseEntity<?> deleteMutualNeighbor(@RequestHeader("Authorization") String token, @RequestBody NeighborMutualAddRequestDto neighborMutualAddRequestDto) {
        if(neighborMutualAddRequestDto.getIsDelete() == null)
            throw new NotValidRequestException("삭제 후 이웃 관계를 설정이 필요합니다.");
        neighborService.deleteMutualNeighbor(token, neighborMutualAddRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "서로 이웃이 삭제 되었습니다."));
    }

    // 사용자가 추가한 이웃 목록
    @Operation(summary = "사용자가 추가한 이웃 목록", description = "회원이 추가한 이웃 목록을 조회합니다.")
    @GetMapping("/{searchId}/to")
    public ResponseEntity<?> getNeighborsTo(@PathVariable("searchId") String searchId) {
        return null;
    }

    // 사용자가 추가한 이웃 목록
    @Operation(summary = "사용자가 추가한 이웃 목록", description = "회원이 추가한 이웃 목록을 조회합니다.")
    @GetMapping("/{searchId}/to")
    public ResponseEntity<?> getNeighborsToCount(@PathVariable("searchId") String searchId) {
        return null;
    }

    // 사용자를 추가한 이웃 목록
    @Operation(summary = "사용자가 추가한 이웃 목록", description = "회원이 추가한 이웃 목록을 조회합니다.")
    @GetMapping("/{searchId}/to/count")
    public ResponseEntity<?> getNeighborsFrom(@PathVariable("searchId") String searchId) {
        return null;
    }

    // 사용자를 추가한 이웃 목록
    @Operation(summary = "사용자가 추가한 이웃 목록", description = "회원이 추가한 이웃 목록을 조회합니다.")
    @GetMapping("/{searchId}/from")
    public ResponseEntity<?> getNeighborsFromCount(@PathVariable("searchId") String searchId) {
        return null;
    }

    // 사용자를 추가한 이웃 목록
    @Operation(summary = "사용자가 추가한 이웃 목록", description = "회원이 추가한 이웃 목록을 조회합니다.")
    @GetMapping("/{searchId}/from/count")
    public ResponseEntity<?> getMutualNeighbors(@PathVariable("searchId") String searchId) {
        return null;
    }
}