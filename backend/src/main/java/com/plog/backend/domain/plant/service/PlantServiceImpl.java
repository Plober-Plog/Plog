package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.domain.image.service.ImageService;
import com.plog.backend.domain.plant.dto.request.PlantAddRequest;
import com.plog.backend.domain.plant.entity.OtherPlantType;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.entity.PlantType;
import com.plog.backend.domain.plant.exception.NotValidPlantTypeIdsException;
import com.plog.backend.domain.plant.repository.PlantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service("plantService")
public class PlantServiceImpl implements PlantService {

    private static final Logger log = LoggerFactory.getLogger(PlantServiceImpl.class);
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private ImageService imageService;

    @Override
    public Plant addPlant(PlantAddRequest plantAddRequest) throws NotValidPlantTypeIdsException {
        // 식물 대표 사진 등록
        Image image = imageService.uploadImage(plantAddRequest.getProfile());
        // 식물 종류 판단
        int type = checkPlantType(plantAddRequest.getPlantTypeId(),
                plantAddRequest.getOtherPlantTypeId());
        switch (type) {
            case 1: // 기본 식물
                PlantType plantType = new PlantType();
                plantType.setPlantTypeId(plantAddRequest.getPlantTypeId());
                Plant plantByPlantType = new Plant(plantType,
                        plantAddRequest.getNickname(),
                        image,
                        plantAddRequest.getBirthDate()
                );
                return plantRepository.save(plantByPlantType);
            case 2: // 기타 식물
                OtherPlantType otherPlantType = new OtherPlantType();
                otherPlantType.setOtherPlantTypeId(plantAddRequest.getOtherPlantTypeId());
                Plant plantByOtherPlantType = new Plant(otherPlantType,
                        plantAddRequest.getNickname(),
                        image,
                        plantAddRequest.getBirthDate()
                );
                return plantRepository.save(plantByOtherPlantType);
            default:
                throw new NotValidPlantTypeIdsException(HttpMethod.POST, HttpStatus.BAD_REQUEST, "/api/user/plant");
        }
    }

    public int checkPlantType(int plantTypeId, int otherPlantTypeId) throws NotValidPlantTypeIdsException {
        if (plantTypeId > 0 && otherPlantTypeId > 0) {
            throw new NotValidPlantTypeIdsException(HttpMethod.POST, HttpStatus.BAD_REQUEST, "/api/user/plant");
        }
        if (plantTypeId > 0) {
            return 1;
        } else if (otherPlantTypeId > 0) {
            return 2;
        } else {
            throw new NotValidPlantTypeIdsException(HttpMethod.POST, HttpStatus.BAD_REQUEST, "/api/user/plant");
        }
    }
}
