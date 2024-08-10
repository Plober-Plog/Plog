package com.plog.backend.domain.plant.controller;

import com.plog.backend.domain.diary.dto.response.PlantDiaryGetResponseDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryGetSimpleResponseDto;
import com.plog.backend.domain.diary.service.PlantDiaryService;
import com.plog.backend.domain.plant.dto.request.*;
import com.plog.backend.domain.plant.dto.response.PlantCheckGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantGetRecordsResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponseDto;
import com.plog.backend.domain.plant.service.PlantCheckService;
import com.plog.backend.domain.plant.service.PlantService;
import com.plog.backend.global.exception.NoTokenRequestException;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.model.response.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/plant")
@Tag(name = "Plant Controller", description = "식물 관리 API")
public class PlantController {

    private final PlantService plantService;
    private final PlantCheckService plantCheckService;
    private final PlantDiaryService plantDiaryService;

    // =============== 식물 ===============
    @PostMapping
    @Operation(summary = "식물 추가", description = "새로운 식물을 등록합니다.")
    public ResponseEntity<BaseResponseBody> addPlant(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "식물 추가 요청 데이터", required = true) @ModelAttribute PlantAddRequestDto plantAddRequestDto) {
        log.info(">>> [POST] /user/plant - 요청 데이터: {}", plantAddRequestDto);
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
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
    @Operation(summary = "식물 정보 조회", description = "식물 ID로 식물의 상세 정보를 조회합니다.")
    public ResponseEntity<PlantGetResponseDto> getPlant(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "식물 ID", required = true) @PathVariable Long plantId) {
        log.info(">>> [GET] /user/plant/{}/info - 요청 ID: {}", plantId, plantId);
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        PlantGetResponseDto plantGetResponseDto = plantService.getPlant(token, plantId);
        return ResponseEntity.status(200).body(plantGetResponseDto);
    }

    @GetMapping("/{plantId}")
    @Operation(summary = "식물 기록 조회", description = "식물 ID와 날짜로 식물의 기록을 조회합니다.")
    public ResponseEntity<PlantGetRecordsResponseDto> getPlantRecordsByDate(
            @Parameter(description = "식물 ID", required = true) @PathVariable Long plantId,
            @Parameter(description = "조회 날짜", required = true) @RequestParam String date) {
        log.info(">>> [GET] /user/plant/{} - 요청 ID: {}, 조회 날짜: {}", plantId, plantId, date);

        PlantGetRecordsResponseDto plantGetRecordsResponseDto = new PlantGetRecordsResponseDto();

        plantGetRecordsResponseDto.setPlantId(plantId);
        plantGetRecordsResponseDto.setDate(LocalDate.parse(date, DateTimeFormatter.ISO_DATE));

        PlantCheckGetResponseDto plantCheckGetResponseDto = plantCheckService.getPlantCheck(plantId, date);
        plantGetRecordsResponseDto.setPlantCheck(plantCheckGetResponseDto.getCheckDate() != null ? plantCheckGetResponseDto : null);

        PlantDiaryGetResponseDto plantDiaryGetResponseDto = plantDiaryService.getPlantDiaryByRecordDate(plantId, date);
        plantGetRecordsResponseDto.setPlantDiary(plantDiaryGetResponseDto != null ? plantDiaryGetResponseDto : null);

        return ResponseEntity.status(200).body(plantGetRecordsResponseDto);
    }

    @GetMapping
    @Operation(summary = "사용자의 식물 목록 조회", description = "사용자 ID로 사용자의 식물 목록을 조회합니다.")
    public ResponseEntity<List<PlantGetResponseDto>> getPlantList(
            @Parameter(description = "사용자 검색 ID", required = true) @RequestParam String searchId,
            @Parameter(description = "식물 종류 ID 목록") @RequestParam(required = false) List<Long> plantTypeId,
            @Parameter(description = "기타 식물 종류 ID 목록") @RequestParam(required = false) List<Long> otherPlantTypeId,
            @Parameter(description = "페이지 번호 : 0이 기본") @RequestParam(required = false, defaultValue = "0") String page) {

        log.info(">>> [GET] /user/plant - 검색 ID: {}, plantTypeId: {}, " +
                        "otherPlantTypeId: {}, page: {}",
                searchId, plantTypeId, otherPlantTypeId, page);

        PlantGetRequestDto plantGetRequestDto = new PlantGetRequestDto();
        plantGetRequestDto.setSearchId(searchId);
        plantGetRequestDto.setPage(Integer.parseInt(page));

        List<PlantGetResponseDto> plantGetResponseDtoList;
        if (plantTypeId != null || otherPlantTypeId != null) {
            log.info("식물 종류 필터링 진행");
            plantGetRequestDto.setPlantTypeId(plantTypeId == null ? new ArrayList<>() : plantTypeId);
            plantGetRequestDto.setOtherPlantTypeId(otherPlantTypeId == null ? new ArrayList<>() : otherPlantTypeId);
            plantGetResponseDtoList = plantService.getPlantListByPlantTypeIds(plantGetRequestDto);
        } else {
            log.info("식물 종류 필터링 진행 안 하고 전체 조회");
            plantGetResponseDtoList = plantService.getPlantList(plantGetRequestDto);
        }

        return ResponseEntity.status(200).body(plantGetResponseDtoList);
    }


    @PatchMapping("/{plantId}")
    @Operation(summary = "식물 정보 수정", description = "식물 ID로 식물 정보를 수정합니다.")
    public ResponseEntity<BaseResponseBody> updatePlant(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "식물 ID", required = true) @PathVariable Long plantId,
            @Parameter(description = "식물 수정 요청 데이터", required = true) @ModelAttribute PlantUpdateRequestDto plantUpdateRequestDto) {
        log.info(">>> [PATCH] /user/plant/{} - 요청 데이터: {}", plantId, plantUpdateRequestDto);
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        plantUpdateRequestDto.setPlantId(plantId);
        plantService.updatePlant(token, plantUpdateRequestDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 수정이 완료되었습니다."));
    }

    @DeleteMapping("/{plantId}")
    @Operation(summary = "식물 삭제", description = "식물 ID로 식물을 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deletePlant(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "식물 ID", required = true) @PathVariable Long plantId) {
        log.info(">>> [DELETE] /user/plant/{} - 삭제 ID: {}", plantId, plantId);
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        plantService.deletePlant(token, plantId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 삭제가 완료되었습니다."));
    }

    @PatchMapping("/{plantId}/farewell")
    @Operation(summary = "식물과 이별", description = "식물 ID로 식물과 이별합니다.")
    public ResponseEntity<BaseResponseBody> farewellPlant(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "식물 ID", required = true) @PathVariable Long plantId) {
        log.info(">>> [PATCH] /user/plant/{}/farewell - 이별 ID: {}", plantId, plantId);
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        plantService.farewellPlant(token, plantId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물과 이별이 완료되었습니다."));
    }

    @PatchMapping("/{plantId}/fix")
    @Operation(summary = "식물 고정 상태 수정", description = "식물 ID로 식물의 고정 여부를 수정합니다.")
    public ResponseEntity<BaseResponseBody> updateFixStatePlant(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "식물 ID", required = true) @PathVariable Long plantId) {
        log.info(">>> [PATCH] /user/plant/{}/fix - 고정할 Id: {}", plantId, plantId);
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        plantService.updateFixStatePlant(token, plantId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물의 고정 여부 수정이 완료되었습니다."));
    }

    @PatchMapping("/{plantId}/notification")
    @Operation(summary = "식물 알림 수신 상태 수정", description = "식물 ID로 식물의 알림 수신 여부를 수정합니다.")
    public ResponseEntity<BaseResponseBody> updateNotificationPlant(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "식물 ID", required = true) @PathVariable Long plantId) {
        log.info(">>> [PATCH] /user/plant/{}/notification - 알림 수신 여부 변경할 Id: {}", plantId, plantId);
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        plantService.updateNotificationPlant(token, plantId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물의 알림 수신 여부 수정이 완료되었습니다."));
    }


    // =============== 관리 ===============

    @PostMapping("/{plantId}/check")
    @Operation(summary = "식물 관리 기록 추가", description = "식물 ID로 식물 관리 여부를 기록합니다.")
    public ResponseEntity<BaseResponseBody> addPlantCheck(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "식물 ID", required = true) @PathVariable Long plantId,
            @Parameter(description = "관리 여부 추가 요청 데이터", required = true) @RequestBody PlantCheckAddRequestDto plantCheckAddRequestDto) {
        log.info(">>> [POST] /user/plant/{}/check - 요청 데이터: {}", plantId, plantCheckAddRequestDto);
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        plantCheckAddRequestDto.setPlantId(plantId);
        plantCheckService.addPlantCheck(token, plantCheckAddRequestDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "관리 여부 기록이 완료되었습니다."));
    }

    @PatchMapping("/{plantId}/check")
    @Operation(summary = "식물 관리 기록 수정", description = "식물 ID로 식물 관리 여부를 수정합니다.")
    public ResponseEntity<BaseResponseBody> updatePlantCheck(
            @Parameter(description = "인증 토큰", required = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "식물 ID", required = true) @PathVariable Long plantId,
            @Parameter(description = "관리 여부 수정 요청 데이터", required = true) @RequestBody PlantCheckUpdateRequestDto plantCheckUpdateRequestDto) {
        log.info(">>> [PATCH] /user/plant/{}/check - 요청 데이터: {}", plantId, plantCheckUpdateRequestDto);
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        plantCheckUpdateRequestDto.setPlantId(plantId);
        plantCheckService.updatePlantCheck(token, plantCheckUpdateRequestDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "관리 여부 수정이 완료되었습니다."));
    }

    @GetMapping("/{plantId}/check")
    @Operation(summary = "식물 관리 기록 조회", description = "식물 ID와 날짜로 식물의 관리 여부를 조회합니다. 날짜 or 연-월 조합으로 진행합니다.")
    public ResponseEntity<?> handlePlantCheck(@Parameter(description = "식물 ID", required = true) @PathVariable Long plantId,
                                              @Parameter(description = "관리 여부 조회 날짜") @RequestParam(required = false) String checkDate,
                                              @Parameter(description = "관리 여부 조회 연도") @RequestParam(required = false) String year,
                                              @Parameter(description = "관리 여부 조회 월") @RequestParam(required = false) String month) {
        if (checkDate != null) {
            log.info(">>> [GET] /user/plant/{}/check - 요청 날짜: {}", plantId, checkDate);

            PlantCheckGetResponseDto plantCheckGetResponseDto = plantCheckService.getPlantCheck(plantId, checkDate);
            return ResponseEntity.status(200).body(plantCheckGetResponseDto);
        } else if (year != null && month != null) {
            log.info(">>> [GET] /user/plant/{}/check - 연도: {}, 월: {}", plantId, year, month);
            PlantGetByYearAndMonthRequestDto plantGetByYearAndMonthRequestDto = new PlantGetByYearAndMonthRequestDto(plantId, Integer.parseInt(year), Integer.parseInt(month));
            List<PlantCheckGetResponseDto> plantCheckGetResponseDtoList = plantCheckService.getPlantCheckByYearAndMonth(plantGetByYearAndMonthRequestDto);
            return ResponseEntity.status(200).body(plantCheckGetResponseDtoList);
        } else {
            throw new NotValidRequestException();
        }
    }

    @DeleteMapping("/{plantId}/check")
    @Operation(summary = "식물 관리 기록 삭제", description = "식물 ID와 날짜로 식물의 관리 여부 기록을 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deletePlantCheck(@Parameter(description = "인증 토큰", required = true) @RequestHeader(value = "Authorization", required = false) String token,
                                                             @Parameter(description = "식물 ID", required = true) @PathVariable Long plantId,
                                                             @Parameter(description = "관리 여부 기록 날짜", required = true) @RequestParam("checkDate") String checkDate) {
        log.info(">>> [DELETE] /user/plant/{}/check - 요청 데이터: {}", plantId, checkDate);
        if(token == null)
            throw new NoTokenRequestException("Access 토큰이 필요합니다.");
        plantCheckService.deletePlantCheck(token, plantId, checkDate);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "관리 여부 삭제가 완료되었습니다."));
    }

    // ================== 일지 ==================
    @GetMapping("/{plantId}/diary")
    @Operation(summary = "식물 일지 조회", description = "식물 ID와 날짜로 식물의 일지를 조회합니다. 만약 연-월이 넘어오지 않았으면 최근 기록 5개를 조회합니다.")
    public ResponseEntity<List<PlantDiaryGetSimpleResponseDto>> getPlantDiaryList(@Parameter(description = "식물 ID", required = true) @PathVariable Long plantId,
                                                                                  @Parameter(description = "조회 연도") @RequestParam(value="year", required = false) String year,
                                                                                  @Parameter(description = "조회 월") @RequestParam(value="month", required = false) String month) {
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
