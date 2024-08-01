package com.plog.backend.domain.plant.controller;

import com.plog.backend.domain.plant.dto.request.PlantRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponse;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.exception.NotValidRequestException;
import com.plog.backend.domain.plant.service.PlantService;
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

    private static PlantService plantService;

    @Autowired
    PlantController(PlantService plantService) {
        PlantController.plantService = plantService;
    }

    @PostMapping
    public ResponseEntity<BaseResponseBody> addPlant(@RequestBody PlantRequestDto plantAddRequest) {
        log.info(">>> [POST] /api/user/plant \t" + plantAddRequest);
        if (plantAddRequest.getNickname() == null || plantAddRequest.getNickname().isEmpty()) {
            throw new NotValidRequestException("nickname 은 필수 필드입니다.");
        }
        if (plantAddRequest.getBirthDate() == null) {
            throw new NotValidRequestException("birthDate 는 필수 필드입니다.");
        }
        plantService.addPlant(plantAddRequest);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 등록이 완료되었습니다."));
    }

    @GetMapping("/{plantId}/info")
    public ResponseEntity<PlantGetResponse> getPlants(@PathVariable Long plantId) {
        log.info(">>> [GET] /api/user/{plantId}/info \t" + plantId);
        PlantGetResponse response = plantService.getPlant(plantId);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PlantGetResponse>> getPlantList(@RequestParam String searchId) {
        log.info(">>> [GET] /api/user/plant?searchId={} \t" + searchId);
        List<PlantGetResponse> response = plantService.getPlantList(searchId);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/{plantId}")
    public ResponseEntity<BaseResponseBody> updatePlant(@PathVariable Long plantId, @RequestBody PlantRequestDto plantUpdateRequest) {
        log.info(">>> [PUT] /api/user/plant \t" + plantId + "\t" + plantUpdateRequest);
        plantService.updatePlant(plantId, plantUpdateRequest);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 수정이 완료되었습니다."));
    }
}
