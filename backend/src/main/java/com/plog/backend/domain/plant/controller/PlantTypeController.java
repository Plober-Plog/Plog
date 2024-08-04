package com.plog.backend.domain.plant.controller;

import com.plog.backend.domain.plant.dto.response.PlantTypeGetResponseDto;
import com.plog.backend.domain.plant.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/plant-type")
public class PlantTypeController {
    private static PlantService plantService;

    @GetMapping("/{plantTypeId}")
    public ResponseEntity<PlantTypeGetResponseDto> getPlantType(@PathVariable Long plantTypeId) {
        PlantTypeGetResponseDto response = plantService.getPlantType(plantTypeId);
        return ResponseEntity.status(200).body(response);
    }
}
