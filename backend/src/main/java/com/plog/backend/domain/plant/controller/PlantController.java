package com.plog.backend.domain.plant.controller;

import com.plog.backend.domain.plant.dto.request.PlantAddRequest;
import com.plog.backend.domain.plant.exception.NotValidRequestException;
import com.plog.backend.domain.plant.service.PlantService;
import com.plog.backend.global.model.response.BaseResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/plant")
public class PlantController {

    @Autowired
    PlantService plantService;

    @PostMapping
    public ResponseEntity<BaseResponseBody> addPlant(@RequestBody PlantAddRequest plantAddRequest) {
        if (plantAddRequest.getNickname() == null || plantAddRequest.getNickname().isEmpty()) {
            throw new NotValidRequestException("nickname 은 필수 필드입니다.");
        }
        if (plantAddRequest.getBirthDate() == null) {
            throw new NotValidRequestException("birthDate 는 필수 필드입니다.");
        }
        plantService.addPlant(plantAddRequest);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "식물 등록이 완료되었습니다."));
    }
}
