package com.plog.backend.domain.plant.controller;

import com.plog.backend.domain.plant.dto.request.PlantAddRequest;
import com.plog.backend.domain.plant.service.PlantService;
import com.plog.backend.global.model.response.BaseResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/plant")
public class PlantController {

    @Autowired
    PlantService plantService;

    @PostMapping
    public ResponseEntity<BaseResponseBody> addPlant(@RequestBody PlantAddRequest plantAddRequest) {
        plantService.addPlant(plantAddRequest);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
    }
}
