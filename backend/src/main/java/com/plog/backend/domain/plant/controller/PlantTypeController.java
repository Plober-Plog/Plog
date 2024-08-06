package com.plog.backend.domain.plant.controller;

import com.plog.backend.domain.plant.dto.response.PlantTypeGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeIdsGetListByUserResponseDto;
import com.plog.backend.domain.plant.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/plant-type")
public class PlantTypeController {
    private final PlantService plantService;

    @GetMapping("/{plantTypeId}")
    public ResponseEntity<PlantTypeGetResponseDto> getPlantType(@PathVariable Long plantTypeId) {
        PlantTypeGetResponseDto plantTypeGetResponseDto = plantService.getPlantType(plantTypeId);
        return ResponseEntity.status(200).body(plantTypeGetResponseDto);
    }

    @GetMapping
    public ResponseEntity<PlantTypeIdsGetListByUserResponseDto> getPlantTypeIdsByUser(@RequestParam String searchId) {
        PlantTypeIdsGetListByUserResponseDto plantTypeIdsGetListByUserResponseDto = plantService.getPlantTypeIdsByUserSearchId(searchId);
        return ResponseEntity.status(200).body(plantTypeIdsGetListByUserResponseDto);
    }
}
