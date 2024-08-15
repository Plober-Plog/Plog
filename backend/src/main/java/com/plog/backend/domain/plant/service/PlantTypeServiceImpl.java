package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.domain.image.repository.ImageRepository;
import com.plog.backend.domain.image.service.ImageService;
import com.plog.backend.domain.plant.dto.request.PlantTypeAddRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetSimpleResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeIdsGetListByUserResponseDto;
import com.plog.backend.domain.plant.entity.PlantType;
import com.plog.backend.domain.plant.repository.PlantRepositorySupport;
import com.plog.backend.domain.plant.repository.PlantTypeRepository;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.model.response.BaseResponseBody;
import jakarta.transaction.Transactional;
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
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Override
    public PlantTypeGetResponseDto getPlantType(Long plantTypeId) {
        Optional<PlantType> plantType = plantTypeRepository.findById(plantTypeId);
        if (plantType.isPresent()) {
            PlantType pt = plantType.get();
            return PlantTypeGetResponseDto.builder()
                    .plantName(pt.getPlantName())
                    .guide(pt.getGuide())
                    .profile(pt.getImage() != null ? pt.getImage().getImageUrl() : null)
                    .waterInterval(pt.getWaterMid())
                    .fertilizeInterval(pt.getFertilizeMid())
                    .repotInterval(pt.getRepotMid())
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

    @Transactional
    @Override
    public BaseResponseBody addPlantType(PlantTypeAddRequestDto request) {
        PlantType plantType = new PlantType();

        if (request.getImage() != null) {
            if (request.getImage().length > 1)
                throw new NotValidRequestException("회원 프로필 사진은 한 장만 등록할 수 있습니다.");
            String[] imageUrl = imageService.uploadImages(request.getImage());
            Image userImage = imageRepository.findByImageUrl(imageUrl[0])
                    .orElseThrow(() -> new EntityNotFoundException("식물 가이드 사진을 불러오는 데 실패하였습니다."));
            plantType.setImage(userImage);
        }

        plantType.setPlantName(request.getPlantName());
        plantType.setGuide(request.getGuide());
        plantType.setWaterInterval(request.getWaterInterval());
        plantType.setFertilizeInterval(request.getFertilizeInterval());
        plantType.setRepotInterval(request.getRepotInterval());
        plantType.setWaterMid(request.getWaterMid());
        plantType.setFertilizeMid(request.getFertilizeMid());
        plantType.setRepotMid(request.getRepotMid());

        plantTypeRepository.save(plantType);

        return BaseResponseBody.of(200, "성공적으로 가이드가 등록 되었습니다.");
    }
}
