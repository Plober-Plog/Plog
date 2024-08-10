package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.plant.dto.request.PlantAddRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantGetRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantUpdateRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponseDto;

import java.util.List;

public interface PlantService {
    void addPlant(String token, PlantAddRequestDto plantAddRequest);

    PlantGetResponseDto getPlant(Long plantId);

    List<PlantGetResponseDto> getPlantList(PlantGetRequestDto plantGetRequestDto);

    List<PlantGetResponseDto> getPlantListByPlantTypeIds(PlantGetRequestDto plantGetRequestDto);

    void updatePlant(String token, PlantUpdateRequestDto plantUpdateRequestDto);

    void deletePlant(String token, Long plantId);

    void farewellPlant(String token, Long plantId);

    void updateFixStatePlant(String token, Long plantId);

    void updateNotificationPlant(String token, Long plantId);

    //TODO [강윤서]
    // - waterDate, fertilizeDate, repotDate 계산하는 로직
}
