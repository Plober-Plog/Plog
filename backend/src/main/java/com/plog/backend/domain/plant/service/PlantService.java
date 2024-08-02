package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.plant.dto.PlantCheckDto;
import com.plog.backend.domain.plant.dto.request.PlantCheckRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantAddRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponse;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetResponse;
import com.plog.backend.domain.plant.exception.NotValidPlantTypeIdsException;

import java.util.List;

public interface PlantService {
    void addPlant(PlantAddRequestDto plantAddRequest) throws NotValidPlantTypeIdsException;

    PlantGetResponse getPlant(Long plantId);

    PlantTypeGetResponse getPlantType(Long plantTypeId);

    List<PlantGetResponse> getPlantList(String searchId);

    void updatePlant(Long plantId, PlantAddRequestDto plantUpdateRequestDto);

    void deletePlant(Long plantId);

    void farewellPlant(Long plantId);

    void addPlantCheck (Long plantId, PlantCheckDto plantCheckDto);

    void updatePlantCheck(Long plantId, PlantCheckDto plantCheckDto);

    PlantCheckDto getPlantCheck(Long plantId, String checkDate);

    void deletePlantCheck(Long plantId, PlantCheckRequestDto checkDate);

    //TODO [강윤서]
    // - 1. isFixed(boolean) 을 fixed(int) 로 변환하는 로직
    // - 2. waterDate, fertilizeDate, repotDate 계산하는 로직
}
