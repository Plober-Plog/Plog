package com.plog.backend.domain.plant.controller;

import com.plog.backend.domain.diary.dto.response.PlantDiaryGetResponseDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryGetSimpleResponseDto;
import com.plog.backend.domain.diary.service.PlantDiaryService;
import com.plog.backend.domain.plant.dto.request.*;
import com.plog.backend.domain.plant.dto.response.PlantCheckGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantGetRecordsResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponseDto;
import com.plog.backend.domain.plant.service.PlantService;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.model.response.BaseResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
            @ModelAttribute PlantAddRequestDto plantAddRequestDto) {
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

    @GetMapping("/{plantId}")
    public ResponseEntity<PlantGetRecordsResponseDto> getPlantRecordsByDate(
            @PathVariable Long plantId,
            @RequestParam String date) {
        log.info(">>> [GET] /user/plant/{} - 요청 ID: {}, 조회 날짜: {}", plantId, plantId, date);

        PlantGetRecordsResponseDto plantGetRecordsResponseDto = new PlantGetRecordsResponseDto();

        PlantCheckGetResponseDto plantCheckGetResponseDto = plantService.getPlantCheck(plantId, date);
        plantGetRecordsResponseDto.setPlantCheck(plantCheckGetResponseDto.getCheckDate() != null ? plantCheckGetResponseDto : null);

        PlantDiaryGetResponseDto plantDiaryGetResponseDto = plantDiaryService.getPlantDiaryByRecordDate(plantId, date);
        plantGetRecordsResponseDto.setPlantDiary(plantDiaryGetResponseDto != null ? plantDiaryGetResponseDto : null);

        return ResponseEntity.status(200).body(plantGetRecordsResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<PlantGetResponseDto>> getPlantList(
            @RequestParam String searchId,
            @RequestParam(required = false) List<Long> plantTypeId,
            @RequestParam(required = false) List<Long> otherPlantTypeId,
            @RequestParam(required = false, defaultValue = "0") String page) {

        log.info(">>> [GET] /user/plant - 검색 ID: {}, plantTypeId: {}, " +
                        "otherPlantTypeId: {}, page: {}",
                searchId, plantTypeId, otherPlantTypeId, page);

        PlantGetRequestDto plantGetRequestDto = new PlantGetRequestDto();
        plantGetRequestDto.setSearchId(searchId);
        plantGetRequestDto.setPage(Integer.parseInt(page));

        List<PlantGetResponseDto> plantGetResponseDtoList;
        log.info(plantTypeId + " " + otherPlantTypeId);
        if (plantTypeId != null && otherPlantTypeId != null) {
            plantGetRequestDto.setPlantTypeId(plantTypeId);
            plantGetRequestDto.setOtherPlantTypeId(otherPlantTypeId);
            plantGetResponseDtoList = plantService.getPlantListByPlantTypeIds(plantGetRequestDto);
        } else {
            plantGetResponseDtoList = plantService.getPlantList(plantGetRequestDto);
        }

        return ResponseEntity.status(200).body(plantGetResponseDtoList);
    }


    @PatchMapping("/{plantId}")
    public ResponseEntity<BaseResponseBody> updatePlant(
            @RequestHeader("Authorization") String token,
            @PathVariable Long plantId,
            @ModelAttribute PlantUpdateRequestDto plantUpdateRequestDto) {
        log.info(">>> [PATCH] /user/plant/{} - 요청 데이터: {}", plantId, plantUpdateRequestDto);
        plantUpdateRequestDto.setPlantId(plantId);
        plantService.updatePlant(token, plantUpdateRequestDto);
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
        plantCheckAddRequestDto.setPlantId(plantId);
        plantService.addPlantCheck(token, plantCheckAddRequestDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "관리 여부 기록이 완료되었습니다."));
    }

    @PatchMapping("/{plantId}/check")
    public ResponseEntity<BaseResponseBody> updatePlantCheck(
            @RequestHeader("Authorization") String token,
            @PathVariable Long plantId,
            @RequestBody PlantCheckUpdateRequestDto plantCheckUpdateRequestDto) {
        log.info(">>> [PATCH] /user/plant/{}/check - 요청 데이터: {}", plantId, plantCheckUpdateRequestDto);
        plantCheckUpdateRequestDto.setPlantId(plantId);
        plantService.updatePlantCheck(token, plantCheckUpdateRequestDto);
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
            PlantGetByYearAndMonthRequestDto plantGetByYearAndMonthRequestDto = new PlantGetByYearAndMonthRequestDto(plantId, Integer.parseInt(year), Integer.parseInt(month));
            List<PlantCheckGetResponseDto> plantCheckGetResponseDtoList = plantService.getPlantCheckByYearAndMonth(plantGetByYearAndMonthRequestDto);
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
    public ResponseEntity<List<PlantDiaryGetSimpleResponseDto>> getPlantDiaryList(@PathVariable Long plantId,
                                                                                  @RequestParam(required = false) String year,
                                                                                  @RequestParam(required = false) String month) {
        if (year != null && month != null) {
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
