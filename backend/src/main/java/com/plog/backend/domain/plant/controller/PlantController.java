package com.plog.backend.domain.plant.controller;

import com.plog.backend.domain.plant.dto.request.PlantAddRequest;
import com.plog.backend.domain.plant.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/plant")
public class PlantController {

//    @Autowired
//    private PlantService plantService;

    @PostMapping
    public PlantAddRequest addPlant(@RequestBody PlantAddRequest plantAddRequest) {
//        plantService.addPlant(plantAddRequest);
        return plantAddRequest;
    }
}
