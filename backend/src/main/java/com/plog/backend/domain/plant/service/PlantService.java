package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.plant.dto.request.*;
import com.plog.backend.domain.plant.dto.response.PlantCheckGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeIdsGetListByUserResponseDto;
import com.plog.backend.domain.plant.exception.NotValidPlantTypeIdsException;

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

    void addPlantCheck (String token, PlantCheckAddRequestDto plantCheckAddRequestDto);

    void updatePlantCheck(String token, PlantCheckUpdateRequestDto plantCheckUpdateRequestDto);

    PlantCheckGetResponseDto getPlantCheck(Long plantId, String checkDate);

    void deletePlantCheck(String token, Long plantId, String checkDate);

    List<PlantCheckGetResponseDto> getPlantCheckByYearAndMonth(PlantGetByYearAndMonthRequestDto plantGetByYearAndMonthRequestDto);

    PlantTypeGetResponseDto getPlantType(Long plantTypeId);

    PlantTypeIdsGetListByUserResponseDto getPlantTypeIdsByUserSearchId(String searchId);

    //TODO [강윤서]
    // - waterDate, fertilizeDate, repotDate 계산하는 로직
}
