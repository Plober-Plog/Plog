package com.plog.backend.domain.plant.controller;

import com.plog.backend.domain.plant.dto.PlantCheckDto;
import com.plog.backend.domain.plant.dto.request.PlantCheckRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantAddRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponse;
import com.plog.backend.domain.plant.service.PlantService;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.model.response.BaseResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/plant")
public class PlantController {

    private final PlantService plantService;

    @Autowired
    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    // 식물
    @PostMapping
    public ResponseEntity<BaseResponseBody> addPlant(@RequestBody PlantAddRequestDto plantAddRequest) {
        log.info(">>> [POST] /user/plant - 요청 데이터: {}", plantAddRequest);
        if (plantAddRequest.getNickname() == null || plantAddRequest.getNickname().isEmpty()) {
            throw new NotValidRequestException("nickname은 필수 필드입니다.");
        }
        if (plantAddRequest.getBirthDate() == null) {
            throw new NotValidRequestException("birthDate는 필수 필드입니다.");
        }
        plantService.addPlant(plantAddRequest);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 등록이 완료되었습니다."));
    }

    @GetMapping("/{plantId}/info")
    public ResponseEntity<PlantGetResponse> getPlants(@PathVariable Long plantId) {
        log.info(">>> [GET] /user/plant/{}/info - 요청 ID: {}", plantId, plantId);
        PlantGetResponse response = plantService.getPlant(plantId);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PlantGetResponse>> getPlantList(@RequestParam String searchId) {
        log.info(">>> [GET] /user/plant - 검색 ID: {}", searchId);
        List<PlantGetResponse> response = plantService.getPlantList(searchId);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/{plantId}")
    public ResponseEntity<BaseResponseBody> updatePlant(@PathVariable Long plantId, @RequestBody PlantAddRequestDto plantUpdateRequest) {
        log.info(">>> [PUT] /user/plant/{} - 요청 데이터: {}", plantId, plantUpdateRequest);
        plantService.updatePlant(plantId, plantUpdateRequest);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 수정이 완료되었습니다."));
    }

    @DeleteMapping("/{plantId}")
    public ResponseEntity<BaseResponseBody> deletePlant(@PathVariable Long plantId) {
        log.info(">>> [DELETE] /user/plant/{} - 삭제 ID: {}", plantId, plantId);
        plantService.deletePlant(plantId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 삭제가 완료되었습니다."));
    }

    @PutMapping("/{plantId}/farewell")
    public ResponseEntity<BaseResponseBody> farewellPlant(@PathVariable Long plantId) {
        log.info(">>> [PUT] /user/plant/{}/farewell - 이별 ID: {}", plantId, plantId);
        plantService.farewellPlant(plantId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물과 이별이 완료되었습니다."));
    }

    // 관리
    @PostMapping("/{plantId}/check")
    public ResponseEntity<BaseResponseBody> addPlantCheck(@PathVariable Long plantId, @RequestBody PlantCheckDto plantCheckDto) {
        log.info(">>> [POST] /user/plant/{}/check - 요청 데이터: {}", plantId, plantCheckDto);
        plantService.addPlantCheck(plantId, plantCheckDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "관리 여부 기록이 완료되었습니다."));
    }

    @PutMapping("/{plantId}/check")
    public ResponseEntity<BaseResponseBody> updatePlantCheck(@PathVariable Long plantId, @RequestBody PlantCheckDto plantCheckDto) {
        log.info(">>> [PUT] /user/plant/{}/check - 요청 데이터: {}", plantId, plantCheckDto);
        plantService.updatePlantCheck(plantId, plantCheckDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "관리 여부 수정이 완료되었습니다."));
    }

    @GetMapping("/{plantId}/check")
    public ResponseEntity<PlantCheckDto> getPlantCheck(@PathVariable Long plantId, @RequestParam("checkDate") String checkDate) {
        log.info(">>> [GET] /user/plant/{}/check - 요청 날짜: {}", plantId, checkDate);
        PlantCheckDto response = plantService.getPlantCheck(plantId, checkDate);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{plantId}/check")
    public ResponseEntity<BaseResponseBody> deletePlantCheck(@PathVariable Long plantId, @RequestBody PlantCheckRequestDto checkDate) {
        log.info(">>> [DELETE] /user/plant/{}/check - 요청 데이터: {}", plantId, checkDate);
        plantService.deletePlantCheck(plantId, checkDate);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "관리 여부 삭제가 완료되었습니다."));
    }
}
