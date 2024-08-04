package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.plant.dto.request.PlantCheckAddRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantAddRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantCheckUpdateRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantUpdateRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantCheckGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetResponseDto;
import com.plog.backend.domain.plant.exception.NotValidPlantTypeIdsException;

import java.util.List;

public interface PlantService {
    void addPlant(String token, PlantAddRequestDto plantAddRequest);

    PlantGetResponseDto getPlant(Long plantId);

    PlantTypeGetResponseDto getPlantType(Long plantTypeId);

    List<PlantGetResponseDto> getPlantList(String searchId);

    List<PlantGetResponseDto> getPlantListByPlantTypeIds(String searchId, String plantTypeId, String otherPlantTypeId);

    void updatePlant(String token, Long plantId, PlantUpdateRequestDto plantUpdateRequestDto);

    void deletePlant(String token, Long plantId);

    void farewellPlant(String token, Long plantId);

    void addPlantCheck (String token, Long plantId, PlantCheckAddRequestDto plantCheckAddRequestDto);

    void updatePlantCheck(String token, Long plantId, PlantCheckUpdateRequestDto plantCheckUpdateRequestDto);

    PlantCheckGetResponseDto getPlantCheck(Long plantId, String checkDate);

    void deletePlantCheck(String token, Long plantId, String checkDate);

    List<PlantCheckGetResponseDto> getPlantCheckByYearAndMonth(Long plantId, String year, String month);

    //TODO [강윤서]
    // - 1. isFixed(boolean) 을 fixed(int) 로 변환하는 로직
    // - 2. waterDate, fertilizeDate, repotDate 계산하는 로직
}
