package com.plog.backend.domain.plant.controller;

import com.plog.backend.domain.plant.dto.response.PlantTypeGetResponse;
import com.plog.backend.domain.plant.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/plant-type")
public class PlantTypeController {
    private static PlantService plantService;

    @Autowired
    PlantTypeController(PlantService plantService) {
        PlantTypeController.plantService = plantService;
    }

    @GetMapping("/{plantTypeId}")
    public ResponseEntity<PlantTypeGetResponse> getPlantType(@PathVariable Long plantTypeId) {
        PlantTypeGetResponse response = plantService.getPlantType(plantTypeId);
        return ResponseEntity.status(200).body(response);
    }
}
