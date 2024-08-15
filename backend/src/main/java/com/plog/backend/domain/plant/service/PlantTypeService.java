package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.plant.dto.request.PlantTypeAddRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetSimpleResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeIdsGetListByUserResponseDto;
import com.plog.backend.global.model.response.BaseResponseBody;

import java.util.List;

public interface PlantTypeService {
    PlantTypeGetResponseDto getPlantType(Long plantTypeId);

    PlantTypeIdsGetListByUserResponseDto getPlantTypeIdsByUserSearchId(String searchId);

    List<PlantTypeGetSimpleResponseDto> getAllPlantTypes();

    BaseResponseBody addPlantType(PlantTypeAddRequestDto plantTypeAddRequestDto);
}
