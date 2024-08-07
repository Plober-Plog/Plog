package com.plog.backend.domain.neighbor.controller;

import com.plog.backend.domain.neighbor.dto.request.NeighborAddRequestDto;
import com.plog.backend.domain.neighbor.dto.request.NeighborMutualAddRequestDto;
import com.plog.backend.domain.neighbor.dto.response.NeighborFromResponseDto;
import com.plog.backend.domain.neighbor.dto.response.NeighborToResponseDto;
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

import java.util.List;

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

    @Operation(summary = "사용자가 추가한 이웃 목록", description = "회원이 추가한 이웃 목록을 조회합니다.")
    @GetMapping("/{searchId}/to")
    public ResponseEntity<List<NeighborToResponseDto>> getNeighborsTo(@PathVariable("searchId") String searchId) {
        List<NeighborToResponseDto> neighbors = neighborService.getNeighborTo(searchId);
        return ResponseEntity.ok(neighbors);
    }

    @Operation(summary = "사용자가 추가한 이웃 수", description = "회원이 추가한 이웃 갯수를 조회합니다.")
    @GetMapping("/{searchId}/to/count")
    public ResponseEntity<BaseResponseBody> getNeighborsToCount(@PathVariable("searchId") String searchId) {
        int count = neighborService.getNeighborToCount(searchId);
        return ResponseEntity.ok(BaseResponseBody.of(200, String.valueOf(count)));
    }

    @Operation(summary = "사용자를 추가한 이웃 목록", description = "회원을 추가한 이웃 목록을 조회합니다.")
    @GetMapping("/{searchId}/from")
    public ResponseEntity<List<NeighborFromResponseDto>> getNeighborsFrom(@PathVariable("searchId") String searchId) {
        List<NeighborFromResponseDto> neighbors = neighborService.getNeighborFrom(searchId);
        return ResponseEntity.ok(neighbors);
    }

    @Operation(summary = "사용자를 추가한 이웃 수", description = "회원을 추가한 이웃 갯수를 조회합니다.")
    @GetMapping("/{searchId}/from/count")
    public ResponseEntity<BaseResponseBody> getNeighborsFromCount(@PathVariable("searchId") String searchId) {
        int count = neighborService.getNeighborFromCount(searchId);
        return ResponseEntity.ok(BaseResponseBody.of(200, String.valueOf(count)));
    }

    @Operation(summary = "사용자의 서로 이웃 목록", description = "회원이 추가한 서로 이웃 목록을 조회합니다.")
    @GetMapping("/{searchId}/mutual")
    public ResponseEntity<List<NeighborToResponseDto>> getMutalNeighborsFrom(@PathVariable("searchId") String searchId) {
        List<NeighborToResponseDto> neighbors = neighborService.getMutualNeighborFrom(searchId);
        return ResponseEntity.ok(neighbors);
    }

    @Operation(summary = "사용자의 서로 이웃 수", description = "회원이 추가한 서로 이웃 수를 조회합니다.")
    @GetMapping("/{searchId}/mutual/count")
    public ResponseEntity<BaseResponseBody> getMutalNeighborsFromCount(@PathVariable("searchId") String searchId) {
        int count = neighborService.getMutualNeighborFromCount(searchId);
        return ResponseEntity.ok(BaseResponseBody.of(200, String.valueOf(count)));
    }

}