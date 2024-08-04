package com.plog.backend.domain.plant.controller;

import com.plog.backend.domain.diary.dto.response.PlantDiaryGetSimpleResponseDto;
import com.plog.backend.domain.diary.service.PlantDiaryService;
import com.plog.backend.domain.plant.dto.request.PlantAddRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantCheckAddRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantCheckUpdateRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantUpdateRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantCheckGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponseDto;
import com.plog.backend.domain.plant.service.PlantService;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.model.response.BaseResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/plant")
public class PlantController {

    private final PlantService plantService;
    private final PlantDiaryService plantDiaryService;

    // =============== 식물 ===============
    @PostMapping
    public ResponseEntity<BaseResponseBody> addPlant(
            @RequestHeader("Authorization") String token,
            @RequestBody PlantAddRequestDto plantAddRequestDto) {
        log.info(">>> [POST] /user/plant - 요청 데이터: {}", plantAddRequestDto);
        if (plantAddRequestDto.getNickname() == null || plantAddRequestDto.getNickname().isEmpty()) {
            throw new NotValidRequestException("nickname은 필수 필드입니다.");
        }
        if (plantAddRequestDto.getBirthDate() == null) {
            throw new NotValidRequestException("birthDate는 필수 필드입니다.");
        }
        plantService.addPlant(token, plantAddRequestDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 등록이 완료되었습니다."));
    }

    @GetMapping("/{plantId}/info")
    public ResponseEntity<PlantGetResponseDto> getPlant(@PathVariable Long plantId) {
        log.info(">>> [GET] /user/plant/{}/info - 요청 ID: {}", plantId, plantId);
        PlantGetResponseDto plantGetResponseDto = plantService.getPlant(plantId);
        return ResponseEntity.status(200).body(plantGetResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<PlantGetResponseDto>> getPlantList(
            @RequestParam String searchId,
            @RequestParam(required = false) String plantTypeId,
            @RequestParam(required = false) String otherPlantTypeId,
            @RequestParam(required = false, defaultValue = "0") String page) {

        log.info(">>> [GET] /user/plant - 검색 ID: {}, plantTypeId: {}, " +
                        "otherPlantTypeId: {}, page: {}",
                searchId, plantTypeId, otherPlantTypeId, page);

        List<PlantGetResponseDto> plantGetResponseDtoList;

        if (plantTypeId != null && otherPlantTypeId != null) {
            plantGetResponseDtoList = plantService.getPlantListByPlantTypeIds(searchId,
                    plantTypeId, otherPlantTypeId, Integer.parseInt(page));
        } else {
            plantGetResponseDtoList = plantService.getPlantList(searchId,
                    Integer.parseInt(page));
        }

        return ResponseEntity.status(200).body(plantGetResponseDtoList);
    }


    @PatchMapping("/{plantId}")
    public ResponseEntity<BaseResponseBody> updatePlant(
            @RequestHeader("Authorization") String token,
            @PathVariable Long plantId,
            @RequestBody PlantUpdateRequestDto plantUpdateRequestDto) {
        log.info(">>> [PATCH] /user/plant/{} - 요청 데이터: {}", plantId, plantUpdateRequestDto);
        plantService.updatePlant(token, plantId, plantUpdateRequestDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 수정이 완료되었습니다."));
    }

    @DeleteMapping("/{plantId}")
    public ResponseEntity<BaseResponseBody> deletePlant(
            @RequestHeader("Authorization") String token,
            @PathVariable Long plantId) {
        log.info(">>> [DELETE] /user/plant/{} - 삭제 ID: {}", plantId, plantId);
        plantService.deletePlant(token, plantId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 삭제가 완료되었습니다."));
    }

    @PatchMapping("/{plantId}/farewell")
    public ResponseEntity<BaseResponseBody> farewellPlant(
            @RequestHeader("Authorization") String token,
            @PathVariable Long plantId) {
        log.info(">>> [PATCH] /user/plant/{}/farewell - 이별 ID: {}", plantId, plantId);
        plantService.farewellPlant(token, plantId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물과 이별이 완료되었습니다."));
    }

    @PatchMapping("/{plantId}/fix")
    public ResponseEntity<BaseResponseBody> updateFixStatePlant(
            @RequestHeader("Authorization") String token,
            @PathVariable Long plantId) {
        log.info(">>> [PATCH] /user/plant/{}/fix - 고정할 Id: {}", plantId, plantId);
        plantService.updateFixStatePlant(token, plantId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물의 고정 여부 수정이 완료되었습니다."));
    }

    // =============== 관리 ===============

    @PostMapping("/{plantId}/check")
    public ResponseEntity<BaseResponseBody> addPlantCheck(
            @RequestHeader("Authorization") String token,
            @PathVariable Long plantId,
            @RequestBody PlantCheckAddRequestDto plantCheckAddRequestDto) {
        log.info(">>> [POST] /user/plant/{}/check - 요청 데이터: {}", plantId, plantCheckAddRequestDto);
        plantService.addPlantCheck(token, plantId, plantCheckAddRequestDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "관리 여부 기록이 완료되었습니다."));
    }

    @PatchMapping("/{plantId}/check")
    public ResponseEntity<BaseResponseBody> updatePlantCheck(
            @RequestHeader("Authorization") String token,
            @PathVariable Long plantId,
            @RequestBody PlantCheckUpdateRequestDto plantCheckUpdateRequestDto) {
        log.info(">>> [PATCH] /user/plant/{}/check - 요청 데이터: {}", plantId, plantCheckUpdateRequestDto);
        plantService.updatePlantCheck(token, plantId, plantCheckUpdateRequestDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "관리 여부 수정이 완료되었습니다."));
    }

    @GetMapping("/{plantId}/check")
    public ResponseEntity<?> handlePlantCheck(@PathVariable Long plantId,
                                              @RequestParam(required = false) String checkDate,
                                              @RequestParam(required = false) String year,
                                              @RequestParam(required = false) String month) {
        if (checkDate != null) {
            log.info(">>> [GET] /user/plant/{}/check - 요청 날짜: {}", plantId, checkDate);
            PlantCheckGetResponseDto plantCheckGetResponseDto = plantService.getPlantCheck(plantId, checkDate);
            return ResponseEntity.status(200).body(plantCheckGetResponseDto);
        } else if (year != null && month != null) {
            log.info(">>> [GET] /user/plant/{}/check - 연도: {}, 월: {}", plantId, year, month);
            List<PlantCheckGetResponseDto> plantCheckGetResponseDtoList = plantService.getPlantCheckByYearAndMonth(plantId, year, month);
            return ResponseEntity.status(200).body(plantCheckGetResponseDtoList);
        } else {
            throw new NotValidRequestException();
        }
    }

    @DeleteMapping("/{plantId}/check")
    public ResponseEntity<BaseResponseBody> deletePlantCheck(@RequestHeader("Authorization") String token, @PathVariable Long plantId, @RequestParam("checkDate") String checkDate) {
        log.info(">>> [DELETE] /user/plant/{}/check - 요청 데이터: {}", plantId, checkDate);
        plantService.deletePlantCheck(token, plantId, checkDate);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "관리 여부 삭제가 완료되었습니다."));
    }

    // ================== 일지 ==================
    @GetMapping("/{plantId}/diary")
    public ResponseEntity<List<PlantDiaryGetSimpleResponseDto>> getPlantDiary(@PathVariable Long plantId,
                                                                              @RequestParam(required = false) String recordDate,
                                                                              @RequestParam(required = false) String year,
                                                                              @RequestParam(required = false) String month) {
        if (recordDate != null) {
            log.info(">>> [GET] /user/plant/{}/diary - 기록 날짜: {}", plantId, recordDate);
            List<PlantDiaryGetSimpleResponseDto> plantDiaryGetResponseDtoList = plantDiaryService.getPlantDiaryByRecordDate(plantId, recordDate);
            return ResponseEntity.status(200).body(plantDiaryGetResponseDtoList);
        } else if (year != null && month != null) {
            log.info(">>> [GET] /user/plant/{}/diary - 연도: {}, 월: {}", plantId, year, month);
            List<PlantDiaryGetSimpleResponseDto> plantDiaryGetResponseDtoList = plantDiaryService.getPlantDiaryByYearAndMonth(plantId, year, month);
            return ResponseEntity.status(200).body(plantDiaryGetResponseDtoList);
        } else {
            log.info(">>> [GET] /user/plant/{}/diary - 최근 5개 일지 조회", plantId);
            List<PlantDiaryGetSimpleResponseDto> plantDiaryGetResponseDtoList = plantDiaryService.getPlantDiaryRecentFive(plantId);
            return ResponseEntity.status(200).body(plantDiaryGetResponseDtoList);
        }
    }
}
