package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.plant.dto.request.PlantAddRequest;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.exception.NotValidPlantTypeIdsException;

public interface PlantService {
    Plant addPlant(PlantAddRequest plantAddRequest) throws NotValidPlantTypeIdsException;
}
