package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.plant.dto.request.PlantAddRequest;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.exception.NotValidPlantTypeIdsException;

public interface PlantService {
    Plant addPlant(PlantAddRequest plantAddRequest) throws NotValidPlantTypeIdsException;

    //TODO [강윤서]
    // - 1. isFixed(boolean) 을 fixed(int) 로 변환하는 로직
    // - 2. waterDate, fertilizeDate, repotDate 계산하는 로직
}
