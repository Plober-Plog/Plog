package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.domain.image.service.ImageService;
import com.plog.backend.domain.plant.dto.PlantCheckDto;
import com.plog.backend.domain.plant.dto.request.PlantCheckRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantAddRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponse;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetResponse;
import com.plog.backend.domain.plant.entity.OtherPlantType;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.entity.PlantCheck;
import com.plog.backend.domain.plant.entity.PlantType;
import com.plog.backend.domain.plant.exception.NotValidPlantTypeIdsException;
import com.plog.backend.domain.plant.repository.OtherPlantTypeRepository;
import com.plog.backend.domain.plant.repository.PlantCheckRepository;
import com.plog.backend.domain.plant.repository.PlantRepository;
import com.plog.backend.domain.plant.repository.PlantTypeRepository;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.service.UserServiceImpl;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    private static OtherPlantTypeRepository otherPlantTypeRepository;
    private static PlantCheckRepository plantCheckRepository;

    @Autowired
    PlantServiceImpl(PlantRepository plantRepository, PlantTypeRepository plantTypeRepository, OtherPlantTypeRepository otherPlantTypeRepository, PlantCheckRepository plantCheckRepository, ImageService imageService, UserServiceImpl userService) {
        PlantServiceImpl.plantRepository = plantRepository;
        PlantServiceImpl.plantTypeRepository = plantTypeRepository;
        PlantServiceImpl.otherPlantTypeRepository = otherPlantTypeRepository;
        PlantServiceImpl.plantCheckRepository = plantCheckRepository;
        PlantServiceImpl.imageService = imageService;
        PlantServiceImpl.userService = userService;
    }

    @Override
    public void addPlant(PlantAddRequestDto plantAddRequest) throws NotValidPlantTypeIdsException {
        log.info(">>> addPlant - 요청 데이터: {}", plantAddRequest);
        Image image = imageService.uploadImage(plantAddRequest.getProfile());
        log.info(">>> addPlant - 이미지 업로드 완료: {}", image);

        int type = checkPlantType(plantAddRequest.getPlantTypeId(), plantAddRequest.getOtherPlantTypeId());
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
                log.info(">>> addPlant - 기본 식물 생성: {}", plantByPlantType);
                plantRepository.save(plantByPlantType);
                return ;
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
                log.info(">>> addPlant - 기타 식물 생성: {}", plantByOtherPlantType);
                plantRepository.save(plantByOtherPlantType);
                return ;
            default:
                throw new NotValidPlantTypeIdsException();
        }
    }

    @Override
    public PlantGetResponse getPlant(Long plantId) {
        log.info(">>> getPlant - 요청 ID: {}", plantId);
        Optional<Plant> plant = plantRepository.findById(plantId);
        if (plant.isPresent()) {
            Plant p = plant.get();
            log.info(">>> getPlant - 식물 정보: {}", p);
            return PlantGetResponse.builder()
                    .plantTypeId(p.getPlantType().getPlantTypeId())
                    .nickname(p.getNickname())
                    .profile(p.getImage().getImageUrl())
                    .birthDate(DateUtil.getInstance().convertToDate(p.getBirthDate()))
                    .hasNotified(p.isHasNotified())
                    .fixed(p.getFixed())
                    .deathDate(DateUtil.getInstance().convertToDate(p.getDeadDate()))
                    .isDeleted(p.isDeleted())
                    .build();
        } else {
            throw new NotValidPlantTypeIdsException();
        }
    }

    @Override
    public PlantTypeGetResponse getPlantType(Long plantTypeId) {
        log.info(">>> getPlantType - 요청 ID: {}", plantTypeId);
        Optional<PlantType> plantType = plantTypeRepository.findById(plantTypeId);
        if (plantType.isPresent()) {
            PlantType p = plantType.get();
            log.info(">>> getPlantType - 식물 타입 정보: {}", p);
            PlantTypeGetResponse response = new PlantTypeGetResponse();
            response.setPlantName(p.getPlantName());
            response.setGuide(p.getGuide());
            response.setProfile(p.getImage().getImageUrl());
            response.setWaterInterval(p.getWaterInterval());
            response.setFertilizeInterval(p.getFertilizeInterval());
            response.setRepotInterval(p.getRepotInterval());
            return response;
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<PlantGetResponse> getPlantList(String searchId) {
        log.info(">>> getPlantList - 검색 ID: {}", searchId);
        User user = userService.getUserBySearchId(searchId);
        if (user != null) {
            List<Plant> list = List.of((Plant) plantRepository.findByUserUserId(user.getUserId()));
            log.info(">>> getPlantList - 검색 결과: {}", list);
            List<PlantGetResponse> response = new ArrayList<>();
            for (Plant p : list) {
                PlantGetResponse pgr = new PlantGetResponse();
                pgr.setPlantTypeId(p.getPlantType().getPlantTypeId());
                pgr.setOtherPlantId(p.getOtherPlantType().getOtherPlantTypeId());
                pgr.setNickname(p.getNickname());
                pgr.setProfile(p.getImage().getImageUrl());
                pgr.setHasNotified(p.isHasNotified());
                pgr.setFixed(p.getFixed());
                pgr.setDeathDate(DateUtil.getInstance().convertToDate(p.getDeadDate()));
                pgr.setDeleted(p.isDeleted());
                response.add(pgr);
            }
            return response;
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public void updatePlant(Long plantId, PlantAddRequestDto plantUpdateRequestDto) {
        log.info(">>> updatePlant - 요청 ID: {}, 업데이트 데이터: {}", plantId, plantUpdateRequestDto);
        int type = checkPlantType(
                plantUpdateRequestDto.getPlantTypeId(),
                plantUpdateRequestDto.getOtherPlantTypeId()
        );
        if (type != 1 && type != 2) throw new NotValidPlantTypeIdsException();

        Optional<Plant> plant = plantRepository.findById(plantId);
        if (plant.isPresent()) {
            Plant p = plant.get();
            if (p.getDeadDate() != null || p.isDeleted()) throw new NotValidRequestException("해당 식물은 삭제 / 이별하여 정보를 수정할 수 없습니다.");
            p.getImage().setImageUrl(plantUpdateRequestDto.getProfile());
            p.setNickname(plantUpdateRequestDto.getNickname());
            p.setBio(plantUpdateRequestDto.getBio());
            p.setBirthDate(DateUtil.getInstance().convertToLocalDate(plantUpdateRequestDto.getBirthDate()));
            p.setHasNotified(plantUpdateRequestDto.isHasNotified());
            p.setFixed(plantUpdateRequestDto.isFixed() ? 1 : 255);
            if (type == 1) {
                p.setPlantType(plantTypeRepository.getReferenceById(plantUpdateRequestDto.getPlantTypeId()));
            } else {
                p.setOtherPlantType(otherPlantTypeRepository.getReferenceById(plantUpdateRequestDto.getOtherPlantTypeId()));
            }
            log.info(">>> updatePlant - 업데이트된 식물 정보: {}", p);
            plantRepository.save(p);
        } else {
            throw new EntityNotFoundException("Plant with ID " + plantId + " not found");
        }
    }

    @Override
    public void deletePlant(Long plantId) {
        log.info(">>> deletePlant - 요청 ID: {}", plantId);
        Optional<Plant> plant = plantRepository.findById(plantId);
        if (plant.isPresent()) {
            Plant p = plant.get();
            if (p.isDeleted()) {
                throw new NotValidRequestException("이미 삭제된 식물입니다.");
            }
            p.setDeleted(true);
            plantRepository.save(p);
            log.info(">>> deletePlant - 식물 삭제 완료, ID: {}", plantId);
        } else {
            throw new EntityNotFoundException("Plant with ID " + plantId + " not found");
        }
    }

    @Override
    public void farewellPlant(Long plantId) {
        log.info(">>> farewellPlant - 요청 ID: {}", plantId);
        Optional<Plant> plant = plantRepository.findById(plantId);
        if (plant.isPresent()) {
            Plant p = plant.get();
            if (p.getDeadDate() != null) {
                throw new NotValidRequestException("이미 이별한 식물입니다.");
            }
            p.setDeadDate(LocalDate.now());
            plantRepository.save(p);
            log.info(">>> farewellPlant - 식물과 이별 완료, ID: {}", plantId);
        } else {
            throw new EntityNotFoundException("Plant with ID " + plantId + " not found");
        }
    }

    @Override
    public void addPlantCheck(Long plantId, PlantCheckDto plantCheckDto) {
        LocalDate checkDate = DateUtil.getInstance().convertToLocalDate(plantCheckDto.getCheckDate());
        if (checkDate.isAfter(LocalDate.now())) {
            throw new NotValidRequestException("미래의 식물 관리 기록은 작성할 수 없습니다");
        }
        Optional<Plant> plant = plantRepository.findById(plantId);
        if (plant.isPresent()) {
            PlantCheck plantCheck = new PlantCheck();
            plantCheck.setPlant(plant.get());
            plantCheck.setCheckDate(checkDate);
            plantCheck.setWatered(plantCheckDto.isWatered());
            plantCheck.setFertilized(plantCheckDto.isFertilized());
            plantCheck.setRepotted(plantCheckDto.isRepotted());
            plantCheckRepository.save(plantCheck);
            log.info(">>> addPlantCheck - 관리 기록 추가 완료, 식물 ID: {}, 관리 날짜: {}", plantId, checkDate);
        } else {
            throw new EntityNotFoundException("Plant with ID " + plantId + " not found");
        }
    }

    @Override
    public void updatePlantCheck(Long plantId, PlantCheckDto plantCheckDto) {
        LocalDate checkDate = DateUtil.getInstance().convertToLocalDate(plantCheckDto.getCheckDate());
        if (checkDate.isAfter(LocalDate.now())) {
            throw new NotValidRequestException("미래의 관리 기록을 수정 할 수 없습니다");
        }
        Optional<Plant> plant = plantRepository.findById(plantId);
        if (plant.isPresent()) {
            Optional<PlantCheck> plantCheck = plantCheckRepository.findByPlantPlantIdAndCheckDate(plantId, checkDate);
            if (plantCheck.isPresent()) {
                PlantCheck pc = plantCheck.get();
                pc.setCheckDate(checkDate);
                pc.setWatered(plantCheckDto.isWatered());
                pc.setFertilized(plantCheckDto.isFertilized());
                pc.setRepotted(plantCheckDto.isRepotted());
                plantCheckRepository.save(pc);
                log.info(">>> updatePlantCheck - 관리 기록 수정 완료, 식물 ID: {}, 관리 날짜: {}", plantId, checkDate);
            } else {
                throw new EntityNotFoundException("No PlantCheck record found for plant ID " + plantId + " and date " + checkDate);
            }
        } else {
            throw new EntityNotFoundException("Plant with ID " + plantId + " not found");
        }
    }

    @Override
    public PlantCheckDto getPlantCheck(Long plantId, String checkDate) {
        log.info(">>> getPlantCheck - 요청 ID: {}, 관리 날짜: {}", plantId, checkDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate date;
        try {
            date = DateUtil.getInstance().convertToLocalDate(dateFormat.parse(checkDate));
        } catch (ParseException e) {
            throw new NotValidRequestException();
        }
        Optional<PlantCheck> plantCheck = plantCheckRepository.findByPlantPlantIdAndCheckDate(plantId, date);
        if (plantCheck.isPresent()) {
            PlantCheck pc = plantCheck.get();
            PlantCheckDto response = new PlantCheckDto();
            response.setCheckDate(DateUtil.getInstance().convertToDate(pc.getCheckDate()));
            response.setWatered(pc.isWatered());
            response.setFertilized(pc.isFertilized());
            response.setRepotted(pc.isRepotted());
            return response;
        } else {
            throw new EntityNotFoundException("No PlantCheck record found for plant ID " + plantId + " and date " + date);
        }
    }

    @Override
    public void deletePlantCheck(Long plantId, PlantCheckRequestDto checkDate) {
        log.info(">>> deletePlantCheck - 요청 ID: {}", plantId);
        LocalDate date = DateUtil.getInstance().convertToLocalDate(checkDate.getCheckDate());
        Optional<PlantCheck> plantCheck = plantCheckRepository.findByPlantPlantIdAndCheckDate(plantId, date);
        if (plantCheck.isPresent()) {
            plantCheckRepository.delete(plantCheck.get());
            log.info(">>> deletePlantCheck - 관리 기록 삭제 완료, 식물 ID: {}, 관리 날짜: {}", plantId, date);
        } else {
            throw new NotValidRequestException("No PlantCheck record found to delete for plant ID " + plantId + " and date " + date);
        }
    }

    public int checkPlantType(Long plantTypeId, Long otherPlantTypeId) throws NotValidPlantTypeIdsException {
        log.info(">>> checkPlantType - 기본 타입 ID: {}, 기타 타입 ID: {}", plantTypeId, otherPlantTypeId);
        if (plantTypeId == null || otherPlantTypeId == null) {
            throw new NotValidPlantTypeIdsException();
        }
        if (plantTypeId > 1L && otherPlantTypeId > 1L) {
            throw new NotValidPlantTypeIdsException();
        }
        if (plantTypeId > 1L) {
            return 1;
        } else if (otherPlantTypeId > 1L) {
            return 2;
        } else {
            throw new NotValidPlantTypeIdsException();
        }
    }
}
