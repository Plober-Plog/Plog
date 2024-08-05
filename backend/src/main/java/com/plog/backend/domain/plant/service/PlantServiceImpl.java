package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.domain.image.repository.ImageRepository;
import com.plog.backend.domain.image.service.ImageService;
import com.plog.backend.domain.plant.dto.request.*;
import com.plog.backend.domain.plant.dto.response.PlantCheckGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeIdsGetListByUserResponseDto;
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
import org.springframework.web.multipart.MultipartFile;

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
    private final ImageRepository imageRepository;

    @Override
    public void addPlant(String token, PlantAddRequestDto plantAddRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        // 식물 대표 사진 등록
        MultipartFile[] images = new MultipartFile[]{plantAddRequestDto.getProfile()};
        if (images.length > 1)
            throw new NotValidRequestException("식물의 대표 사진은 한 장만 등록할 수 있습니다.");
        String[] imageUrl = imageService.uploadImages(images);

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Optional<Image> plantImage = imageRepository.findByImageUrl(imageUrl[0]);
            if (!plantImage.isPresent()) {
                throw new EntityNotFoundException("식물의 대표 사진을 불러오는 데 실패하였습니다.");
            }

            PlantType plantType = new PlantType();
            OtherPlantType otherPlantType = new OtherPlantType();

            // 식물 종류 판별하기
            if (plantAddRequestDto.getPlantTypeId() != null
                    && plantAddRequestDto.getPlantTypeId() >= 2
            ) { // 1. 기본 식물
                plantType = plantTypeRepository.getReferenceById(plantAddRequestDto.getPlantTypeId());
                otherPlantType = otherPlantTypeRepository.getReferenceById(1L);

                Plant plantByPlantType = Plant.builder()
                        .user(user.get())
                        .plantType(plantType)
                        .otherPlantType(otherPlantType)
                        .nickname(plantAddRequestDto.getNickname())
                        .image(plantImage.get())
                        .birthDate(plantAddRequestDto.getBirthDate())
                        .bio(plantAddRequestDto.getBio())
                        .build();
                log.info(">>> addPlant - 기본 식물 생성: {}", plantByPlantType.getPlantId());
                plantRepository.save(plantByPlantType);
            } else { // 2. 기타 식물
                if (plantAddRequestDto.getOtherPlantName() == null
                        || plantAddRequestDto.getOtherPlantName() == "")
                    throw new NotValidRequestException("등록할 식물의 종류가 없습니다.");

                plantType = plantTypeRepository.getReferenceById(1L);

                OtherPlantType existingOtherPlantType = otherPlantTypeRepository.findByPlantName(plantAddRequestDto.getOtherPlantName());
                if (existingOtherPlantType != null) { // 이미 등록된 기타 식물
                    otherPlantType.setOtherPlantTypeId(existingOtherPlantType.getOtherPlantTypeId());
                    otherPlantType.setPlantName(existingOtherPlantType.getPlantName());
                    log.info(">>> 이미 존재하는 기타 식물 : {}", existingOtherPlantType.getPlantName());
                } else {
                    OtherPlantType newOtherPlantType = otherPlantTypeRepository.save(
                            new OtherPlantType(plantAddRequestDto.getOtherPlantName())
                    );
                    otherPlantType.setOtherPlantTypeId(newOtherPlantType.getOtherPlantTypeId());
                    otherPlantType.setPlantName(newOtherPlantType.getPlantName());
                    log.info(">>> 새로 추가된 기타 식물 : {}", existingOtherPlantType.getPlantName());
                }

                Plant plantByOtherPlantType = Plant.builder()
                        .user(user.get())
                        .plantType(plantType)
                        .otherPlantType(otherPlantType)
                        .nickname(plantAddRequestDto.getNickname())
                        .image(plantImage.get())
                        .birthDate(plantAddRequestDto.getBirthDate())
                        .bio(plantAddRequestDto.getBio())
                        .build();
                log.info(">>> addPlant - 기타 식물 생성: {}", plantByOtherPlantType.getPlantId());
                plantRepository.save(plantByOtherPlantType);
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
            log.info(">>> getPlant - 식물 조회 완료: {}", plantId);
            return PlantGetResponseDto.builder()
                    .plantId(p.getPlantId())
                    .plantTypeId(p.getPlantType().getPlantTypeId())
                    .nickname(p.getNickname())
                    .bio(p.getBio())
                    .profile(p.getImage() != null ? p.getImage().getImageUrl() : null)
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
                        .profile(p.getImage() != null ? p.getImage().getImageUrl() : null)
                        .isDeleted(p.isDeleted())
                        .isFixed(p.isFixed())
                        .build();
                plantGetResponseDtoList.add(pgr);
            }
            log.info(">>> getPlantList - 회원 {}의 식물 목록 조회 완료: {}", user.get().getSearchId());
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
                    log.info(">>> getPlantListByPlantTypeIds - 회원 {}의 기본 식물 목록 조회", user.get().getSearchId());
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
                                .profile(p.getImage() != null ? p.getImage().getImageUrl() : null)
                                .isDeleted(p.isDeleted())
                                .isFixed(p.isFixed())
                                .build();
                        plantGetResponseDtoList.add(pgr);
                    }
                    return plantGetResponseDtoList;
                }
                case 2 -> {
                    log.info(">>> getPlantListByPlantTypeIds - 회원 {}의 기타 식물 목록 조회", user.get().getSearchId());
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
                                .profile(p.getImage() != null ? p.getImage().getImageUrl() : null)
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
    public void updatePlant(String token, PlantUpdateRequestDto plantUpdateRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        // 식물 대표 사진 변경
        MultipartFile[] images = new MultipartFile[]{plantUpdateRequestDto.getProfile()};
        if (images.length > 1)
            throw new NotValidRequestException("식물의 대표 사진은 한 장만 등록할 수 있습니다.");
        String[] imageUrl = imageService.uploadImages(images);

        Optional<Plant> plant = plantRepository.findById(plantUpdateRequestDto.getPlantId());
        if (plant.isPresent()) {
            Plant p = plant.get();

            if (p.getDeadDate() != null || p.isDeleted())
                throw new NotValidRequestException("해당 식물은 삭제 / 이별하여 정보를 수정할 수 없습니다.");
            if (userId != p.getUser().getUserId())
                throw new NotAuthorizedRequestException();

            Optional<Image> plantImage = imageRepository.findByImageUrl(imageUrl[0]);
            if (!plantImage.isPresent()) {
                throw new EntityNotFoundException("식물의 대표 사진을 불러오는 데 실패하였습니다.");
            }

            // 식물 종류 판별하기
            if (plantUpdateRequestDto.getPlantTypeId() != null
                    && plantUpdateRequestDto.getPlantTypeId() >= 2
            ) { // 1. 기본 식물
                p.setPlantType(plantTypeRepository.getReferenceById(plantUpdateRequestDto.getPlantTypeId()));
                p.setOtherPlantType(otherPlantTypeRepository.getReferenceById(1L));
                log.info(">>> 기본 식물 : {}", p.getPlantType().getPlantName());
            } else { // 2. 기타 식물
                if (plantUpdateRequestDto.getOtherPlantName() == null
                        || plantUpdateRequestDto.getOtherPlantName() == "")
                    throw new NotValidRequestException("수정할 식물의 종류가 없습니다.");

                p.setPlantType(plantTypeRepository.getReferenceById(1L));
                OtherPlantType existingOtherPlantType = otherPlantTypeRepository.findByPlantName(plantUpdateRequestDto.getOtherPlantName());
                if (existingOtherPlantType != null) {
                    p.setOtherPlantType(existingOtherPlantType);
                    log.info(">>> 이미 존재하는 기타 식물 : {}", existingOtherPlantType.getPlantName());
                } else {
                    OtherPlantType newOtherPlantType = otherPlantTypeRepository.save(
                            new OtherPlantType(plantUpdateRequestDto.getOtherPlantName())
                    );
                    p.setOtherPlantType(newOtherPlantType);
                    log.info(">>> 새로 추가된 기타 식물 : {}", newOtherPlantType.getPlantName());
                }
            }

            // 남은 정보 업데이트
            p.setImage(plantImage.get());
            p.setNickname(plantUpdateRequestDto.getNickname());
            p.setBio(plantUpdateRequestDto.getBio());
            p.setBirthDate(plantUpdateRequestDto.getBirthDate());

            log.info(">>> updatePlant - 식물 업데이트 완료, ID: {}", p.getPlantId());
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
    public void addPlantCheck(String token, PlantCheckAddRequestDto plantCheckAddRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        if (plantCheckAddRequestDto.getCheckDate().isAfter(LocalDate.now())) {
            throw new NotValidRequestException("미래의 식물 관리 기록은 작성할 수 없습니다");
        }
        Optional<Plant> plant = plantRepository.findById(plantCheckAddRequestDto.getPlantId());
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
            log.info(">>> addPlantCheck - 관리 기록 추가 완료, 식물 ID: {}, 관리 날짜: {}", plantCheckAddRequestDto.getPlantId(), plantCheckAddRequestDto.getCheckDate());
        } else {
            throw new EntityNotFoundException("일치하는 식물이 없습니다.");
        }
    }

    @Transactional
    @Override
    public void updatePlantCheck(String token, PlantCheckUpdateRequestDto plantCheckUpdateRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        if (plantCheckUpdateRequestDto.getCheckDate().isAfter(LocalDate.now())) {
            throw new NotValidRequestException("미래의 관리 기록을 수정 할 수 없습니다");
        }
        Optional<Plant> plant = plantRepository.findById(plantCheckUpdateRequestDto.getPlantId());
        if (plant.isPresent()) {
            if (userId != plant.get().getUser().getUserId())
                throw new NotAuthorizedRequestException();
            Optional<PlantCheck> plantCheck = plantCheckRepository.findByPlantPlantIdAndCheckDate(plantCheckUpdateRequestDto.getPlantId(), plantCheckUpdateRequestDto.getCheckDate());
            if (plantCheck.isPresent()) {
                PlantCheck pc = plantCheck.get();
                pc.setCheckDate(plantCheckUpdateRequestDto.getCheckDate());
                pc.setWatered(plantCheckUpdateRequestDto.isWatered());
                pc.setFertilized(plantCheckUpdateRequestDto.isFertilized());
                pc.setRepotted(plantCheckUpdateRequestDto.isRepotted());
                plantCheckRepository.save(pc);
                log.info(">>> updatePlantCheck - 관리 기록 수정 완료, 식물 ID: {}, 관리 날짜: {}", plantCheckUpdateRequestDto.getCheckDate(), plantCheckUpdateRequestDto.getCheckDate());
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
            log.info(">>> updatePlantCheck - 관리 기록 조회 완료 {}", plantCheck.get());
            PlantCheck pc = plantCheck.get();
            PlantCheckGetResponseDto plantCheckGetResponseDto = new PlantCheckGetResponseDto();
            plantCheckGetResponseDto.setCheckDate(pc.getCheckDate());
            plantCheckGetResponseDto.setWatered(pc.isWatered());
            plantCheckGetResponseDto.setFertilized(pc.isFertilized());
            plantCheckGetResponseDto.setRepotted(pc.isRepotted());
            return plantCheckGetResponseDto;
        } else {
            return new PlantCheckGetResponseDto();
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
    public List<PlantCheckGetResponseDto> getPlantCheckByYearAndMonth(PlantGetByYearAndMonthRequestDto plantGetByYearAndMonthRequestDto) {
        LocalDate startDate = LocalDate.of(plantGetByYearAndMonthRequestDto.getYear(), plantGetByYearAndMonthRequestDto.getMonth(), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Optional<List<PlantCheck>> optionalPlantChecks = plantCheckRepository.findAllByPlantPlantId(plantGetByYearAndMonthRequestDto.getPlantId());
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
        log.info(">>> getPlantTypeIdsByUserSearchId: {}", plantTypeIdsGetListByUserResponseDto);
        return plantTypeIdsGetListByUserResponseDto;
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