package com.plog.backend.domain.plant.controller;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.domain.plant.dto.request.PlantTypeAddRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetSimpleResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeIdsGetListByUserResponseDto;
import com.plog.backend.domain.plant.service.PlantTypeService;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.model.response.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/plant-type")
@Tag(name = "Plant Type Controller", description = "식물 종류 API")
public class PlantTypeController {
    private final PlantTypeService plantTypeService;

    @GetMapping("/{plantTypeId}")
    @Operation(summary = "가이드 기능을 위한 식물 종류 상세 조회", description = "식물 종류 ID로 식물 종류의 상세 정보를 조회합니다.")
    public ResponseEntity<PlantTypeGetResponseDto> getPlantType(@PathVariable("plantTypeId") Long plantTypeId) {
        log.info(">>> [GET] /user/plant-type/{} - 요청 데이터: {}", plantTypeId);
        PlantTypeGetResponseDto plantTypeGetResponseDto = plantTypeService.getPlantType(plantTypeId);
        return ResponseEntity.status(200).body(plantTypeGetResponseDto);
    }

    @GetMapping
    @Operation(summary = "사용자의 식물 종류 목록 조회", description = "사용자의 검색 ID로 보유한 식물 종류를 조회합니다.")
    public ResponseEntity<PlantTypeIdsGetListByUserResponseDto> getPlantTypeIdsByUser(@RequestParam String searchId) {
        log.info(">>> [GET] /user/plant-type : {}가 보유한 식물 종류 반환", searchId);
        PlantTypeIdsGetListByUserResponseDto plantTypeIdsGetListByUserResponseDto = plantTypeService.getPlantTypeIdsByUserSearchId(searchId);
        return ResponseEntity.status(200).body(plantTypeIdsGetListByUserResponseDto);
    }

    @GetMapping("/all")
    @Operation(summary = "전체 식물 종류 목록 조회", description = "등록된 식물 종류 목록을 조회합니다.")
    public ResponseEntity<List<PlantTypeGetSimpleResponseDto>> getAllPlantTypes() {
        log.info(">>> [GET] /user/plant-type/all : 모든 식물 종류 리스트 반환");
        List<PlantTypeGetSimpleResponseDto> plantTypeGetSimpleResponseDtoList = plantTypeService.getAllPlantTypes();
        return ResponseEntity.status(200).body(plantTypeGetSimpleResponseDtoList);
    }

    @PostMapping
    public ResponseEntity<?> setGuide(
            @Parameter(description = "식물 추가 요청 데이터", required = true)
            @ModelAttribute PlantTypeAddRequestDto plantTypeAddRequestDto
    ) {
        return ResponseEntity.ok(plantTypeService.addPlantType(plantTypeAddRequestDto));
    }

}
