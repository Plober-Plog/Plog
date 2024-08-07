package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.plant.dto.response.PlantTypeGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetSimpleResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeIdsGetListByUserResponseDto;
import com.plog.backend.domain.plant.entity.PlantType;
import com.plog.backend.domain.plant.repository.PlantRepositorySupport;
import com.plog.backend.domain.plant.repository.PlantTypeRepository;
import com.plog.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service("plantTypeService")
public class PlantTypeServiceImpl implements PlantTypeService {
    private final PlantTypeRepository plantTypeRepository;
    private final PlantRepositorySupport plantRepositorySupport;

    @Override
    public PlantTypeGetResponseDto getPlantType(Long plantTypeId) {
        Optional<PlantType> plantType = plantTypeRepository.findById(plantTypeId);
        if (plantType.isPresent()) {
            PlantType pt = plantType.get();
            return PlantTypeGetResponseDto.builder()
                    .plantName(pt.getPlantName())
                    .guide(pt.getGuide())
                    .profile(pt.getImage() != null ? pt.getImage().getImageUrl() : null)
                    .waterInterval(pt.getWaterInterval())
                    .fertilizeInterval(pt.getFertilizeInterval())
                    .repotInterval(pt.getRepotInterval())
                    .build();
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public PlantTypeIdsGetListByUserResponseDto getPlantTypeIdsByUserSearchId(String searchId) {
        PlantTypeIdsGetListByUserResponseDto plantTypeIdsGetListByUserResponseDto = new PlantTypeIdsGetListByUserResponseDto();

        plantTypeIdsGetListByUserResponseDto.setPlantTypes(
                plantRepositorySupport.findDistinctPlantTypeIdtByUserSearchId(searchId)
        );
        plantTypeIdsGetListByUserResponseDto.setOtherPlantTypes(
                plantRepositorySupport.findDistinctOtherPlantTypeIdByUserSearchId(searchId)
        );
        log.info(">>> getPlantTypeIdsByUserSearchId 조회 완료");
        return plantTypeIdsGetListByUserResponseDto;
    }

    @Override
    public List<PlantTypeGetSimpleResponseDto> getAllPlantTypes() {
        List<PlantTypeGetSimpleResponseDto> plantTypeGetSimpleResponseDtoList = new ArrayList<>();
        plantTypeRepository.findAll().forEach(plantType -> {
            plantTypeGetSimpleResponseDtoList.add(new PlantTypeGetSimpleResponseDto(plantType.getPlantTypeId(), plantType.getPlantName()));
        });
        log.info(">>> getAllPlantTypes 조회 완료");
        return plantTypeGetSimpleResponseDtoList;
    }
}
