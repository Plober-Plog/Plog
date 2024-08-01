package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.plant.dto.request.PlantRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponse;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetResponse;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.exception.NotValidPlantTypeIdsException;

import java.util.List;

public interface PlantService {
    Plant addPlant(PlantRequestDto plantAddRequest) throws NotValidPlantTypeIdsException;

    PlantGetResponse getPlant(Long plantId);

    PlantTypeGetResponse getPlantType(Long plantTypeId);

    List<PlantGetResponse> getPlantList(String searchId);

    Plant updatePlant(Long plantId, PlantRequestDto plantUpdateRequestDto);
    //TODO [강윤서]
    // - 1. isFixed(boolean) 을 fixed(int) 로 변환하는 로직
    // - 2. waterDate, fertilizeDate, repotDate 계산하는 로직
}
