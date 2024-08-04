package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.image.service.ImageService;
import com.plog.backend.domain.plant.dto.request.*;
import com.plog.backend.domain.plant.dto.response.PlantCheckGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetResponseDto;
import com.plog.backend.domain.plant.entity.OtherPlantType;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.entity.PlantCheck;
import com.plog.backend.domain.plant.entity.PlantType;
import com.plog.backend.domain.plant.exception.NotValidPlantTypeIdsException;
import com.plog.backend.domain.plant.repository.*;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotAuthorizedRequestException;
import com.plog.backend.global.exception.NotValidRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.plog.backend.global.util.JwtTokenUtil.jwtTokenUtil;

@Slf4j
@RequiredArgsConstructor
@Service("plantService")
public class PlantServiceImpl implements PlantService {

    private final PlantRepository plantRepository;
    private final PlantRepositorySupport plantRepositorySupport;
    private final PlantTypeRepository plantTypeRepository;
    private final OtherPlantTypeRepository otherPlantTypeRepository;
    private final PlantCheckRepository plantCheckRepository;
    private final UserRepository userRepository;

    private final ImageService imageService;

    @Override
    public void addPlant(String token, PlantAddRequestDto plantAddRequest) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);

//        Image image = imageService.uploadImage(plantAddRequest.getProfile());
//        log.info(">>> addPlant - 이미지 업로드 완료: {}", image);

        int type = checkPlantType(plantAddRequest.getPlantTypeId(), plantAddRequest.getOtherPlantTypeId());

        PlantType plantType = new PlantType();
        OtherPlantType otherPlantType = new OtherPlantType();

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            switch (type) {
                case 1 -> { // 기본 식물
                    plantType.setPlantTypeId(plantAddRequest.getPlantTypeId());
                    otherPlantType.setOtherPlantTypeId(1L);

                    Plant plantByPlantType = Plant.builder()
                            .user(user.get())
                            .plantType(plantType)
                            .otherPlantType(otherPlantType)
                            .nickname(plantAddRequest.getNickname())
//                            .image(image)
                            .birthDate(plantAddRequest.getBirthDate())
                            .bio(plantAddRequest.getBio())
                            .build();
                    log.info(">>> addPlant - 기본 식물 생성: {}", plantByPlantType);
                    plantRepository.save(plantByPlantType);
                }
                case 2 -> {// 기타 식물
                    plantType.setPlantTypeId(1L);
                    otherPlantType.setOtherPlantTypeId(plantAddRequest.getOtherPlantTypeId());

                    Plant plantByOtherPlantType = Plant.builder()
                            .user(user.get())
                            .plantType(plantType)
                            .otherPlantType(otherPlantType)
                            .nickname(plantAddRequest.getNickname())
//                            .image(image)
                            .birthDate(plantAddRequest.getBirthDate())
                            .bio(plantAddRequest.getBio())
                            .build();
                    log.info(">>> addPlant - 기타 식물 생성: {}", plantByOtherPlantType);
                    plantRepository.save(plantByOtherPlantType);
                }
                default -> throw new NotValidPlantTypeIdsException();
            }
        } else {
            throw new EntityNotFoundException("일치하는 회원이 없습니다.");
        }
    }

    @Override
    public PlantGetResponseDto getPlant(Long plantId) {
        Optional<Plant> plant = plantRepository.findById(plantId);
        if (plant.isPresent()) {
            Plant p = plant.get();
            return PlantGetResponseDto.builder()
                    .plantId(p.getPlantId())
                    .plantTypeId(p.getPlantType().getPlantTypeId())
                    .nickname(p.getNickname())
                    .bio(p.getBio())
//                    .profile(p.getImage().getImageUrl())
                    .birthDate(p.getBirthDate())
                    .hasNotified(p.isHasNotified())
                    .isFixed(p.isFixed())
                    .deadDate(p.getDeadDate())
                    .isDeleted(p.isDeleted())
                    .build();
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public PlantTypeGetResponseDto getPlantType(Long plantTypeId) {
        Optional<PlantType> plantType = plantTypeRepository.findById(plantTypeId);
        if (plantType.isPresent()) {
            PlantType pt = plantType.get();
            return PlantTypeGetResponseDto.builder()
                    .plantName(pt.getPlantName())
                    .guide(pt.getGuide())
//                    .profile(pt.getImage().getImageUrl())
                    .waterInterval(pt.getWaterInterval())
                    .fertilizeInterval(pt.getFertilizeInterval())
                    .repotInterval(pt.getRepotInterval())
                    .build();
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<PlantGetResponseDto> getPlantList(PlantGetRequestDto plantGetRequestDto) {
        Optional<User> user = userRepository.findUserBySearchId(plantGetRequestDto.getSearchId());
        if (user.isPresent()) {
            List<PlantGetResponseDto> plantGetResponseDtoList = new ArrayList<>();
            List<Plant> plantList = plantRepositorySupport.findByUserSearchId(plantGetRequestDto.getSearchId(), plantGetRequestDto.getPage());
            for (Plant p : plantList) {
                PlantGetResponseDto pgr = PlantGetResponseDto.builder()
                        .plantId(p.getPlantId())
                        .plantTypeId(p.getPlantType().getPlantTypeId())
                        .otherPlantId(p.getOtherPlantType().getOtherPlantTypeId())
                        .birthDate(p.getBirthDate())
                        .deadDate(p.getDeadDate())
                        .nickname(p.getNickname())
                        .bio(p.getBio())
//                        .profile(p.getImage().getImageUrl())
                        .isDeleted(p.isDeleted())
                        .isFixed(p.isFixed())
                        .build();
                plantGetResponseDtoList.add(pgr);
            }
            return plantGetResponseDtoList;
        } else {
            throw new EntityNotFoundException("일치하는 회원이 없습니다.");
        }
    }

    @Override
    public List<PlantGetResponseDto> getPlantListByPlantTypeIds(PlantGetRequestDto plantGetRequestDto) {
        Optional<User> user = userRepository.findUserBySearchId(plantGetRequestDto.getSearchId());
        if (user.isPresent()) {
            int type = checkPlantType(plantGetRequestDto.getPlantTypeId(), plantGetRequestDto.getOtherPlantTypeId());
            switch (type) {
                case 1 -> {
                    List<PlantGetResponseDto> plantGetResponseDtoList = new ArrayList<>();
                    List<Plant> plantList = plantRepositorySupport.findByUserSearchIdAndPlantTypeId(plantGetRequestDto.getSearchId(), plantGetRequestDto.getPlantTypeId(), plantGetRequestDto.getPage());
                    for (Plant p : plantList) {
                        PlantGetResponseDto pgr = PlantGetResponseDto.builder()
                                .plantId(p.getPlantId())
                                .plantTypeId(p.getPlantType().getPlantTypeId())
                                .otherPlantId(p.getOtherPlantType().getOtherPlantTypeId())
                                .birthDate(p.getBirthDate())
                                .deadDate(p.getDeadDate())
                                .nickname(p.getNickname())
                                .bio(p.getBio())
//                                .profile(p.getImage().getImageUrl())
                                .isDeleted(p.isDeleted())
                                .isFixed(p.isFixed())
                                .build();
                        plantGetResponseDtoList.add(pgr);
                    }
                    return plantGetResponseDtoList;
                }
                case 2 -> {
                    List<PlantGetResponseDto> plantGetResponseDtoList = new ArrayList<>();
                    List<Plant> plantList = plantRepositorySupport.findByUserSearchIdAndOtherPlantTypeId(plantGetRequestDto.getSearchId(), plantGetRequestDto.getOtherPlantTypeId(), plantGetRequestDto.getPage());
                    for (Plant p : plantList) {
                        PlantGetResponseDto pgr = PlantGetResponseDto.builder()
                                .plantId(p.getPlantId())
                                .plantTypeId(p.getPlantType().getPlantTypeId())
                                .otherPlantId(p.getOtherPlantType().getOtherPlantTypeId())
                                .birthDate(p.getBirthDate())
                                .deadDate(p.getDeadDate())
                                .nickname(p.getNickname())
                                .bio(p.getBio())
//                                .profile(p.getImage().getImageUrl())
                                .isDeleted(p.isDeleted())
                                .isFixed(p.isFixed())
                                .build();
                        plantGetResponseDtoList.add(pgr);
                    }
                    return plantGetResponseDtoList;
                }
                default -> throw new NotValidPlantTypeIdsException();
            }
        } else {
            throw new EntityNotFoundException("일치하는 회원이 없습니다.");
        }
    }

    @Transactional
    @Override
    public void updatePlant(String token, Long plantId, PlantUpdateRequestDto plantUpdateRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        int type = checkPlantType(
                plantUpdateRequestDto.getPlantTypeId(),
                plantUpdateRequestDto.getOtherPlantTypeId()
        );
        if (type != 1 && type != 2) throw new NotValidPlantTypeIdsException();

        Optional<Plant> plant = plantRepository.findById(plantId);
        if (plant.isPresent()) {
            Plant p = plant.get();

            if (p.getDeadDate() != null || p.isDeleted())
                throw new NotValidRequestException("해당 식물은 삭제 / 이별하여 정보를 수정할 수 없습니다.");
            if (userId != p.getUser().getUserId())
                throw new NotAuthorizedRequestException();

//            p.getImage().setImageUrl(plantUpdateRequestDto.getProfile());
            p.setNickname(plantUpdateRequestDto.getNickname());
            p.setBio(plantUpdateRequestDto.getBio());
            p.setBirthDate(plantUpdateRequestDto.getBirthDate());
            p.setPlantType(plantTypeRepository.getReferenceById(plantUpdateRequestDto.getPlantTypeId()));
            p.setOtherPlantType(otherPlantTypeRepository.getReferenceById(plantUpdateRequestDto.getOtherPlantTypeId()));
            log.info(">>> updatePlant - 업데이트된 식물 정보: {}", p);
            plantRepository.save(p);
        } else {
            throw new EntityNotFoundException("일치하는 식물이 없습니다.");
        }
    }

    @Transactional
    @Override
    public void deletePlant(String token, Long plantId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        Optional<Plant> plant = plantRepository.findById(plantId);
        if (plant.isPresent()) {
            Plant p = plant.get();
            if (userId != p.getUser().getUserId())
                throw new NotAuthorizedRequestException();
            if (p.isDeleted()) {
                throw new NotValidRequestException("이미 삭제된 식물입니다.");
            }
            p.setDeleted(true);
            plantRepository.save(p);
            log.info(">>> deletePlant - 식물 삭제 완료, ID: {}", plantId);
        } else {
            throw new EntityNotFoundException("일치하는 식물이 없습니다.");
        }
    }

    @Transactional
    @Override
    public void farewellPlant(String token, Long plantId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        Optional<Plant> plant = plantRepository.findById(plantId);
        if (plant.isPresent()) {
            Plant p = plant.get();
            if (userId != p.getUser().getUserId())
                throw new NotAuthorizedRequestException();
            if (p.getDeadDate() != null) {
                throw new NotValidRequestException("이미 이별한 식물입니다.");
            }
            p.setDeadDate(LocalDate.now());
            plantRepository.save(p);
            log.info(">>> farewellPlant - 식물과 이별 완료, ID: {}", plantId);
        } else {
            throw new EntityNotFoundException("일치하는 식물이 없습니다.");
        }
    }

    @Transactional
    @Override
    public void updateFixStatePlant(String token, Long plantId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        Optional<Plant> plant = plantRepository.findById(plantId);
        if (plant.isPresent()) {
            Plant p = plant.get();
            if (userId != p.getUser().getUserId())
                throw new NotAuthorizedRequestException();
            if (p.isFixed()) {
                p.setFixed(false);
                p.setFixedAt(null);
            } else {
                p.setFixed(true);
                p.setFixedAt(LocalDateTime.now());
            }
            plantRepository.save(p);
            log.info(">>> updateFixStatePlant - 식물 고정 여부 수정 완료, ID: {}", plantId);
        } else {
            throw new EntityNotFoundException("일치하는 식물이 없습니다.");
        }
    }

    @Override
    public void addPlantCheck(String token, Long plantId, PlantCheckAddRequestDto plantCheckAddRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        if (plantCheckAddRequestDto.getCheckDate().isAfter(LocalDate.now())) {
            throw new NotValidRequestException("미래의 식물 관리 기록은 작성할 수 없습니다");
        }
        Optional<Plant> plant = plantRepository.findById(plantId);
        if (plant.isPresent()) {
            if (userId != plant.get().getUser().getUserId())
                throw new NotAuthorizedRequestException();
            PlantCheck plantCheck = PlantCheck.builder()
                    .plant(plant.get())
                    .isWatered(plantCheckAddRequestDto.isWatered())
                    .isFertilized(plantCheckAddRequestDto.isFertilized())
                    .isRepotted(plantCheckAddRequestDto.isRepotted())
                    .checkDate(plantCheckAddRequestDto.getCheckDate())
                    .build();
            plantCheckRepository.save(plantCheck);
            log.info(">>> addPlantCheck - 관리 기록 추가 완료, 식물 ID: {}, 관리 날짜: {}", plantId, plantCheckAddRequestDto.getCheckDate());
        } else {
            throw new EntityNotFoundException("일치하는 식물이 없습니다.");
        }
    }

    @Transactional
    @Override
    public void updatePlantCheck(String token, Long plantId, PlantCheckUpdateRequestDto plantCheckUpdateRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        if (plantCheckUpdateRequestDto.getCheckDate().isAfter(LocalDate.now())) {
            throw new NotValidRequestException("미래의 관리 기록을 수정 할 수 없습니다");
        }
        Optional<Plant> plant = plantRepository.findById(plantId);
        if (plant.isPresent()) {
            if (userId != plant.get().getUser().getUserId())
                throw new NotAuthorizedRequestException();
            Optional<PlantCheck> plantCheck = plantCheckRepository.findByPlantPlantIdAndCheckDate(plantId, plantCheckUpdateRequestDto.getCheckDate());
            if (plantCheck.isPresent()) {
                PlantCheck pc = plantCheck.get();
                pc.setCheckDate(plantCheckUpdateRequestDto.getCheckDate());
                pc.setWatered(plantCheckUpdateRequestDto.isWatered());
                pc.setFertilized(plantCheckUpdateRequestDto.isFertilized());
                pc.setRepotted(plantCheckUpdateRequestDto.isRepotted());
                plantCheckRepository.save(pc);
                log.info(">>> updatePlantCheck - 관리 기록 수정 완료, 식물 ID: {}, 관리 날짜: {}", plantId, plantCheckUpdateRequestDto.getCheckDate());
            } else {
                throw new EntityNotFoundException("일치하는 식물 관리 기록이 없습니다.");
            }
        } else {
            throw new EntityNotFoundException("일치하는 식물이 없습니다.");
        }
    }

    @Override
    public PlantCheckGetResponseDto getPlantCheck(Long plantId, String checkDate) {
        LocalDate date = LocalDate.parse(checkDate, DateTimeFormatter.ISO_DATE);

        Optional<PlantCheck> plantCheck = plantCheckRepository.findByPlantPlantIdAndCheckDate(plantId, date);
        if (plantCheck.isPresent()) {
            PlantCheck pc = plantCheck.get();
            PlantCheckGetResponseDto plantCheckGetResponseDto = new PlantCheckGetResponseDto();
            plantCheckGetResponseDto.setCheckDate(pc.getCheckDate());
            plantCheckGetResponseDto.setWatered(pc.isWatered());
            plantCheckGetResponseDto.setFertilized(pc.isFertilized());
            plantCheckGetResponseDto.setRepotted(pc.isRepotted());
            return plantCheckGetResponseDto;
        } else {
            throw new EntityNotFoundException("일치하는 식물 관리 기록이 없습니다.");
        }
    }

    @Transactional
    @Override
    public void deletePlantCheck(String token, Long plantId, String checkDate) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        LocalDate date = LocalDate.parse(checkDate, DateTimeFormatter.ISO_DATE);
        Optional<Plant> plant = plantRepository.findById(plantId);
        if (plant.isPresent()) {
            if (userId != plant.get().getUser().getUserId())
                throw new NotAuthorizedRequestException();
        }
        Optional<PlantCheck> plantCheck = plantCheckRepository.findByPlantPlantIdAndCheckDate(plantId, date);
        if (plantCheck.isPresent()) {
            plantCheckRepository.delete(plantCheck.get());
            log.info(">>> deletePlantCheck - 관리 기록 삭제 완료, 식물 ID: {}, 관리 날짜: {}", plantId, date);
        } else {
            throw new EntityNotFoundException("일치하는 식물이 없습니다.");
        }
    }

    @Override
    public List<PlantCheckGetResponseDto> getPlantCheckByYearAndMonth(Long plantId, String year, String month) {
        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);

        LocalDate startDate = LocalDate.of(yearInt, monthInt, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Optional<List<PlantCheck>> optionalPlantChecks = plantCheckRepository.findAllByPlantPlantId(plantId);
        List<PlantCheckGetResponseDto> result = new ArrayList<>();

        if (optionalPlantChecks.isPresent()) {
            List<PlantCheck> plantChecks = optionalPlantChecks.get();
            for (PlantCheck plantCheck : plantChecks) {
                LocalDate checkDate = plantCheck.getCheckDate();
                if (!checkDate.isBefore(startDate) && !checkDate.isAfter(endDate)) {

                    result.add(PlantCheckGetResponseDto.builder()
                            .checkDate(checkDate)
                            .isWatered(plantCheck.isWatered())
                            .isFertilized(plantCheck.isFertilized())
                            .isRepotted(plantCheck.isRepotted())
                            .build());
                }
            }
        }
        return result;
    }

    public long getPlantTypeSize() {
        return plantTypeRepository.count();
    }

    public int checkPlantType(Long plantTypeId, Long otherPlantTypeId) throws NotValidPlantTypeIdsException {
        log.info(">>> checkPlantType - 기본 타입 ID: {}, 기타 타입 ID: {}", plantTypeId, otherPlantTypeId);
        if (plantTypeId == null || otherPlantTypeId == null) {
            throw new NotValidPlantTypeIdsException();
        }
        if (plantTypeId > 1L && otherPlantTypeId > 1L) {
            throw new NotValidPlantTypeIdsException();
        }
        if (plantTypeId > 1L && plantTypeId < getPlantTypeSize()) {
            return 1;
        } else if (otherPlantTypeId > 1L) {
            return 2;
        } else {
            throw new NotValidPlantTypeIdsException();
        }
    }
}