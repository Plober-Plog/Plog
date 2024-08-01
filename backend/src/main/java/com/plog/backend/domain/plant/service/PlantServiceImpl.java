package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.domain.image.service.ImageService;
import com.plog.backend.domain.plant.dto.request.PlantRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponse;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetResponse;
import com.plog.backend.domain.plant.entity.OtherPlantType;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.entity.PlantType;
import com.plog.backend.domain.plant.exception.NotValidPlantTypeIdsException;
import com.plog.backend.domain.plant.repository.PlantRepository;
import com.plog.backend.domain.plant.repository.PlantTypeRepository;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.service.UserServiceImpl;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service("plantService")
public class PlantServiceImpl implements PlantService {

    private static PlantRepository plantRepository;
    private static PlantTypeRepository plantTypeRepository;
    private static ImageService imageService;
    private static UserServiceImpl userService;

    @Autowired
    PlantServiceImpl(PlantRepository plantRepository, ImageService imageService, PlantTypeRepository plantTypeRepository, UserServiceImpl userService) {
        PlantServiceImpl.plantRepository = plantRepository;
        PlantServiceImpl.plantTypeRepository = plantTypeRepository;
        PlantServiceImpl.imageService = imageService;
        PlantServiceImpl.userService = userService;
    }

    @Override
    public Plant addPlant(PlantRequestDto plantAddRequest) throws NotValidPlantTypeIdsException {
        // 식물 대표 사진 등록
        Image image = imageService.uploadImage(plantAddRequest.getProfile());
        // 식물 종류 판단
        int type = checkPlantType(plantAddRequest.getPlantTypeId(),
                plantAddRequest.getOtherPlantTypeId());
        PlantType plantType = new PlantType();
        OtherPlantType otherPlantType = new OtherPlantType();

        switch (type) {
            case 1: // 기본 식물
                plantType.setPlantTypeId(plantAddRequest.getPlantTypeId());
                otherPlantType.setOtherPlantTypeId(1L);
                Plant plantByPlantType = new Plant(plantType, otherPlantType,
                        plantAddRequest.getNickname(),
                        image,
                        plantAddRequest.getBirthDate(),
                        plantAddRequest.isHasNotified(),
                        plantAddRequest.isFixed()
                );
                return plantRepository.save(plantByPlantType);
            case 2: // 기타 식물
                plantType.setPlantTypeId(1L);
                otherPlantType.setOtherPlantTypeId(plantAddRequest.getOtherPlantTypeId());
                Plant plantByOtherPlantType = new Plant(plantType, otherPlantType,
                        plantAddRequest.getNickname(),
                        image,
                        plantAddRequest.getBirthDate(),
                        plantAddRequest.isHasNotified(),
                        plantAddRequest.isFixed()
                );
                return plantRepository.save(plantByOtherPlantType);
            default:
                throw new NotValidPlantTypeIdsException();
        }
    }

    @Override
    public PlantGetResponse getPlant(Long plantId) {
        Optional<Plant> plant = plantRepository.findById(plantId);
        log.info(">>> /api/user/plant/{}", plantId + "\t" + plant);
        if (plant.isPresent()) {
            return PlantGetResponse.builder()
                    .plantTypeId(plant.get().getPlantType().getPlantTypeId())
                    .nickname(plant.get().getNickname())
                    .profile(plant.get().getImage().getImageUrl())
                    .birthDate(DateUtil.getInstance().convertToDate(plant.get().getBirthDate()))
                    .hasNotified(plant.get().isHasNotified())
                    .fixed(plant.get().getFixed())
                    .build();
        } else {
            throw new NotValidPlantTypeIdsException();
        }
    }

    @Override
    public PlantTypeGetResponse getPlantType(Long plantTypeId) {
       Optional<PlantType> plantType = plantTypeRepository.findById(plantTypeId);
       log.info(">>> /api/user/plant-type/{}", plantTypeId + "\t" + plantType);
       if (plantType.isPresent()) {
           PlantType p = plantType.get();
           PlantTypeGetResponse response = new PlantTypeGetResponse();
           response.setPlantName(p.getPlantName());
           response.setGuide(p.getGuide());
           response.setProfile(p.getImage().getImageUrl());
           response.setWaterInterval(p.getWaterInterval());
           response.setFertilizeInterval(p.getFertilizeInterval());
           response.setRepotInterval(p.getRepotInterval());
           return response;
       }else {
           throw new EntityNotFoundException();
       }
    }

    @Override
    public List<PlantGetResponse> getPlantList(String searchId) {
        User user = userService.getUserBySearchId(searchId);
        if (user != null) {
            List<Plant> list = List.of((Plant) plantRepository.findByUserUserId(user.getUserId()));
            log.info(">>>>>> " + list);
            List<PlantGetResponse> response = new ArrayList<>();
            for (Plant p : list) {
                PlantGetResponse pgr = new PlantGetResponse();
                pgr.setPlantTypeId(p.getPlantType().getPlantTypeId());
                pgr.setOtherPlantId(p.getOtherPlantType().getOtherPlantTypeId());
                pgr.setNickname(p.getNickname());
                pgr.setProfile(p.getImage().getImageUrl());
                pgr.setHasNotified(p.isHasNotified());
                pgr.setFixed(p.getFixed());
            }
            return response;
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Plant updatePlant(Long plantId, PlantRequestDto plantUpdateRequestDto) {
        int type = checkPlantType(
                plantUpdateRequestDto.getPlantTypeId(),
                plantUpdateRequestDto.getOtherPlantTypeId()
        );

        Optional<Plant> plant = plantRepository.findById(plantId);
        if (plant.isPresent()) {
            Plant p = plant.get();
            p.getPlantType().setPlantTypeId(plantUpdateRequestDto.getPlantTypeId());
            p.getOtherPlantType().setOtherPlantTypeId(plantUpdateRequestDto.getOtherPlantTypeId());
            p.getImage().setImageUrl(plantUpdateRequestDto.getProfile());
            p.setNickname(plantUpdateRequestDto.getNickname());
            p.setBio(plantUpdateRequestDto.getBio());
            p.setBirthDate(DateUtil.getInstance().convertToLocalDate(plantUpdateRequestDto.getBirthDate()));
            p.setHasNotified(plantUpdateRequestDto.isHasNotified());
            p.setFixed(plantUpdateRequestDto.isFixed() ? 1 : 255);
            log.info(">>>>>> " +  p);
            return plantRepository.save(p);
        } else {
            throw new EntityNotFoundException("Plant with ID " + plantId + " not found");
        }
    }

    public int checkPlantType(Long plantTypeId, Long otherPlantTypeId) throws NotValidPlantTypeIdsException {
        if (plantTypeId == null || otherPlantTypeId == null) {
            throw new NotValidPlantTypeIdsException();
        }
        if (plantTypeId > 1L && otherPlantTypeId > 1L) {
            throw new NotValidPlantTypeIdsException();
        }
        if (plantTypeId > 1L) { // TODO [강윤서] - 식물 종 개수 세기
            return 1;
        } else if (otherPlantTypeId > 1L) {
            return 2;
        } else {
            throw new NotValidPlantTypeIdsException();
        }
    }
}
