package com.plog.backend.domain.diary.controller;

import com.plog.backend.domain.diary.dto.request.PlantDiaryAddRequestDto;
import com.plog.backend.domain.diary.dto.request.PlantDiaryImageUploadRequestDto;
import com.plog.backend.domain.diary.dto.request.PlantDiaryUpdateRequestDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryGetResponseDto;
import com.plog.backend.domain.diary.service.PlantDiaryService;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.model.response.BaseResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/diary")
public class PlantDiaryController {
    private final PlantDiaryService plantDiaryService;

    @PostMapping
    public ResponseEntity<BaseResponseBody> addPlantDiary(
            @RequestHeader("Authorization") String token,
            @ModelAttribute PlantDiaryAddRequestDto plantDiaryAddRequestDto,
        @RequestPart List<PlantDiaryImageUploadRequestDto> images) {
        log.info(">>> [POST] /user/diary - 요청 데이터: {}", plantDiaryAddRequestDto);
        plantDiaryService.addPlantDiary(token, plantDiaryAddRequestDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 일지 등록이 완료되었습니다."));
    }

    @PatchMapping("/{plantDiaryId}")
    public ResponseEntity<BaseResponseBody> updatePlantDiary(
            @RequestHeader("Authorization") String token,
            @PathVariable Long plantDiaryId,
            @RequestBody PlantDiaryUpdateRequestDto plantDiaryUpdateRequestDto) {
        log.info(">>> [PATCH] /user/diary/{} - 요청 데이터: {}", plantDiaryId, plantDiaryUpdateRequestDto);
        plantDiaryUpdateRequestDto.setPlantDiaryId(plantDiaryId);
        plantDiaryService.updatePlantDiary(token, plantDiaryUpdateRequestDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 일지 수정이 완료되었습니다."));
    }

    @DeleteMapping("/{plantDiaryId}")
    public ResponseEntity<BaseResponseBody> deletePlantDiary(
            @RequestHeader("Authorization") String token,
            @PathVariable Long plantDiaryId) {
        log.info(">>> [DELETE] /user/diary/{} - 삭제 ID: {}", plantDiaryId, plantDiaryId);
        plantDiaryService.deletePlantDiary(token, plantDiaryId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 일지 삭제가 완료되었습니다."));
    }

    @GetMapping("/{plantDiaryId}")
    public ResponseEntity<PlantDiaryGetResponseDto> getPlantDiary(
            @PathVariable Long plantDiaryId) {
        log.info(">>> [GET] /user/diary/{} - 요청 ID: {}", plantDiaryId, plantDiaryId);
        PlantDiaryGetResponseDto plantDiaryGetResponseDto = plantDiaryService.getPlantDiary(plantDiaryId);
        return ResponseEntity.status(200).body(plantDiaryGetResponseDto);
    }

    @GetMapping("/{plantId}")
    public ResponseEntity<PlantDiaryGetResponseDto> getPlantDiary(
            @PathVariable Long plantId,
            @RequestParam(required = false) String recordDate) {
        if (recordDate == null)
            throw new NotValidRequestException("recordDate는 필수 값입니다.");
        log.info(">>> [GET] /user/diary/{} - 요청 ID: {}, 기록 일자: {}", plantId, plantId, recordDate);
        PlantDiaryGetResponseDto plantDiaryGetResponseDto = plantDiaryService.getPlantDiaryByRecordDate(plantId, recordDate);
        return ResponseEntity.status(200).body(plantDiaryGetResponseDto);
    }
}
