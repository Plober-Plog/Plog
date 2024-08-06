package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.plant.dto.request.PlantCheckAddRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantCheckUpdateRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantGetByYearAndMonthRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantCheckGetResponseDto;

import java.util.List;

public interface PlantCheckService {
    void addPlantCheck (String token, PlantCheckAddRequestDto plantCheckAddRequestDto);

    void updatePlantCheck(String token, PlantCheckUpdateRequestDto plantCheckUpdateRequestDto);

    PlantCheckGetResponseDto getPlantCheck(Long plantId, String checkDate);

    void deletePlantCheck(String token, Long plantId, String checkDate);

    List<PlantCheckGetResponseDto> getPlantCheckByYearAndMonth(PlantGetByYearAndMonthRequestDto plantGetByYearAndMonthRequestDto);

}
