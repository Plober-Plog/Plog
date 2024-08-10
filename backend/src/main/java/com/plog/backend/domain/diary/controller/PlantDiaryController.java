package com.plog.backend.domain.diary.controller;

import com.plog.backend.domain.diary.dto.request.PlantDiaryAddRequestDto;
import com.plog.backend.domain.diary.dto.request.PlantDiaryImageUploadRequestDto;
import com.plog.backend.domain.diary.dto.request.PlantDiaryUpdateRequestDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryGetResponseDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryWeatherGetResponseDto;
import com.plog.backend.domain.diary.service.PlantDiaryService;
import com.plog.backend.global.exception.NoTokenRequestException;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.model.response.BaseResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/diary")
@Tag(name = "Plant Diary Controller", description = "식물 일지 API")
public class PlantDiaryController {
    private final PlantDiaryService plantDiaryService;

    @GetMapping("/get-weather")
    public ResponseEntity<PlantDiaryWeatherGetResponseDto> getWeather(
            @RequestHeader("Authorization") String token,
            @RequestParam String date
    ) {

        if(token.isEmpty() || token.isBlank())
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        if (date == null)
            throw new NotValidRequestException("date 값은 필수 값입니다.");
        log.info(">>> [GET] /api/user/diary/get-weather   토큰: {}, 날짜: {}", token, date);
        PlantDiaryWeatherGetResponseDto plantDiaryWeatherGetResponseDto =  plantDiaryService.getWeatherData(token, date);
        return ResponseEntity.status(200).body(plantDiaryWeatherGetResponseDto);
    }

    @PostMapping
    public ResponseEntity<BaseResponseBody> addPlantDiary(
            @RequestHeader("Authorization") String token,
            @ModelAttribute PlantDiaryAddRequestDto plantDiaryAddRequestDto,
            @RequestPart(value = "images", required = false) MultipartFile[] images) {
        if(token.isEmpty() || token.isBlank())
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        if (plantDiaryAddRequestDto.getPlantId() == null)
            throw new NotValidRequestException("plantId 는 필수값입니다.");
        if (plantDiaryAddRequestDto.getRecordDate() == null) {
            throw new NotValidRequestException("recordDate 는 필수값입니다.");
        }
        log.info(">>> [POST] /user/diary - 요청 데이터: {} 이미지 여부: {}", plantDiaryAddRequestDto, images == null ? "X" : "O");
        Long plantDiaryId = plantDiaryService.addPlantDiary(token, plantDiaryAddRequestDto);
        // 요청으로 넘어온 이미지 리스트가 있으면 호출
        if (images != null && images.length > 0) {
            plantDiaryService.uploadPlantDiaryImages(images, plantDiaryAddRequestDto.getThumbnailIdx(), plantDiaryId);
        }
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 일지 등록이 완료되었습니다."));
    }

    @PatchMapping("/{plantDiaryId}")
    public ResponseEntity<BaseResponseBody> updatePlantDiary(
            @RequestHeader("Authorization") String token,
            @PathVariable Long plantDiaryId,
            @RequestBody PlantDiaryUpdateRequestDto plantDiaryUpdateRequestDto) {
        log.info(">>> [PATCH] /user/diary/{} - 요청 데이터: {}", plantDiaryId, plantDiaryUpdateRequestDto);
        if(token.isEmpty() || token.isBlank())
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        if (plantDiaryUpdateRequestDto.getRecordDate() == null)
            throw new NotValidRequestException("일지 기록 일자는 필수 값입니다.");
        if (plantDiaryUpdateRequestDto.getPlantId() == null)
            throw new NotValidRequestException("식물 번호는 필수값입니다.");
        plantDiaryUpdateRequestDto.setPlantDiaryId(plantDiaryId);
        plantDiaryService.updatePlantDiary(token, plantDiaryUpdateRequestDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 일지 수정이 완료되었습니다."));
    }

    @DeleteMapping("/{plantDiaryId}")
    public ResponseEntity<BaseResponseBody> deletePlantDiary(
            @RequestHeader("Authorization") String token,
            @PathVariable Long plantDiaryId) {
        log.info(">>> [DELETE] /user/diary/{} - 삭제 ID: {}", plantDiaryId, plantDiaryId);
        if(token.isEmpty() || token.isBlank())
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
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

    @GetMapping("/plant/{plantId}")
    public ResponseEntity<PlantDiaryGetResponseDto> getPlantDiary(
            @PathVariable Long plantId,
            @RequestParam String recordDate) {
        if (recordDate == null)
            throw new NotValidRequestException("recordDate는 필수 값입니다.");
        log.info(">>> [GET] /user/diary/{} - 요청 ID: {}, 기록 일자: {}", plantId, plantId, recordDate);
        PlantDiaryGetResponseDto plantDiaryGetResponseDto = plantDiaryService.getPlantDiaryByRecordDate(plantId, recordDate);
        return ResponseEntity.status(200).body(plantDiaryGetResponseDto);
    }
}
