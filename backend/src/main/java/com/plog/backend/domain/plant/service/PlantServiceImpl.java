package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.domain.image.repository.ImageRepository;
import com.plog.backend.domain.image.service.ImageService;
import com.plog.backend.domain.plant.dto.request.PlantAddRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantGetRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantUpdateRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponseDto;
import com.plog.backend.domain.plant.entity.OtherPlantType;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.entity.PlantType;
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
    private final UserRepository userRepository;

    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final PlantCheckRepositorySupport plantCheckRepositorySupport;

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

                Plant plantByPlantType = plantRepository.save(Plant.builder()
                        .user(user.get())
                        .plantType(plantType)
                        .otherPlantType(otherPlantType)
                        .nickname(plantAddRequestDto.getNickname())
                        .image(plantImage.get())
                        .birthDate(plantAddRequestDto.getBirthDate())
                        .bio(plantAddRequestDto.getBio())
                        .notifySetting(7)
                        .fertilizeDate(plantAddRequestDto.getBirthDate().plusDays(plantType.getFertilizeMid()))
                        .repotDate(plantAddRequestDto.getBirthDate().plusDays(plantType.getRepotMid()))
                        .waterDate(plantAddRequestDto.getBirthDate().plusDays(plantType.getWaterMid()))
                        .build());
                log.info(">>> addPlant - 기본 식물 생성: {}", plantByPlantType.getPlantId());
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
                    log.info(">>> 새로 추가된 기타 식물 : {}", newOtherPlantType.getPlantName());
                }

                Plant plantByOtherPlantType = plantRepository.save(Plant.builder()
                        .user(user.get())
                        .plantType(plantType)
                        .otherPlantType(otherPlantType)
                        .nickname(plantAddRequestDto.getNickname())
                        .image(plantImage.get())
                        .birthDate(plantAddRequestDto.getBirthDate())
                        .bio(plantAddRequestDto.getBio())
                        .build());
                log.info(">>> addPlant - 기타 식물 생성: {}", plantByOtherPlantType.getPlantId());

            }
        } else {
            throw new EntityNotFoundException("일치하는 회원이 없습니다.");
        }
    }

    @Override
    public PlantGetResponseDto getPlant(String token, Long plantId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        Plant plant = plantRepository.findById(plantId).orElseThrow(
                () -> new EntityNotFoundException("일치하는 식물을 조회할 수 업습니다.")
        );
        if (userId != plant.getUser().getUserId())
            throw new NotAuthorizedRequestException();

        log.info(">>> getPlant - 식물 조회 완료: {}", plantId);
        return PlantGetResponseDto.builder()
                .plantId(plant.getPlantId())
                .plantTypeId(plant.getPlantType().getPlantTypeId())
                .nickname(plant.getNickname())
                .bio(plant.getBio())
                .profile(plant.getImage() != null ? plant.getImage().getImageUrl() : null)
                .plantTypeName(plant.getPlantType().getPlantName())
                .otherPlantId(plant.getOtherPlantType().getOtherPlantTypeId())
                .otherPlantTypeName(plant.getOtherPlantType().getPlantName())
                .birthDate(plant.getBirthDate())
                .hasNotification(plant.getNotifySetting() == 7 ? true : false)
                .isFixed(plant.isFixed())
                .deadDate(plant.getDeadDate())
                .isDeleted(plant.isDeleted())
                .build();
    }

    @Override
    public List<PlantGetResponseDto> getPlantList(PlantGetRequestDto plantGetRequestDto) {
        Optional<User> user = userRepository.findUserBySearchId(plantGetRequestDto.getSearchId());
        if (user.isPresent()) {
            List<PlantGetResponseDto> plantGetResponseDtoList = plantRepositorySupport.findByUserSearchId(plantGetRequestDto.getSearchId(), plantGetRequestDto.getPage());
            log.info(">>> getPlantList - 회원 {}의 식물 목록 조회 완료", user.get().getSearchId());
            return plantGetResponseDtoList;
        } else {
            throw new EntityNotFoundException("일치하는 회원이 없습니다.");
        }
    }

    @Override
    public List<PlantGetResponseDto> getPlantListByPlantTypeIds(PlantGetRequestDto plantGetRequestDto) {
        Optional<User> user = userRepository.findUserBySearchId(plantGetRequestDto.getSearchId());
        List<PlantGetResponseDto> plantGetResponseDtoList = new ArrayList<>();
        if (user.isPresent()) {
            String searchId = plantGetRequestDto.getSearchId();
            for (Long plantTypeId : plantGetRequestDto.getPlantTypeId()) {
                plantGetResponseDtoList.addAll(plantRepositorySupport.findByUserSearchIdAndPlantTypeId(searchId, plantTypeId, plantGetRequestDto.getPage()));
            }
            for (Long otherPlantTypeId : plantGetRequestDto.getOtherPlantTypeId()) {
                plantGetResponseDtoList.addAll(plantRepositorySupport.findByUserSearchIdAndOtherPlantTypeId(searchId, otherPlantTypeId, plantGetRequestDto.getPage()));
            }
            return plantGetResponseDtoList;
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

    @Transactional
    @Override
    public void updateNotificationPlant(String token, Long plantId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        Plant plant = plantRepository.findById(plantId).orElseThrow(
                () -> new EntityNotFoundException("일치하는 식물이 없습니다.")
        );
        if (userId != plant.getUser().getUserId())
            throw new NotAuthorizedRequestException();
        if (plant.getNotifySetting() == 7)
            plant.setNotifySetting(0);
        else
            plant.setNotifySetting(7);
        plantRepository.save(plant);
        log.info(">>> updateNotificationPlant - 식물 알림 수신 여부 수정 완료, ID: {}", plantId);
    }

//    @Override
//    public void calculateNextDateToWater(Long plantId) {
//        Plant plant = plantRepository.findById(plantId).orElseThrow(() -> new EntityNotFoundException("식물 정보가 없습니다."));
//        // 1. 마지막으로 물을 준 날자 가져오기 (plantCheck 에 값 기록될 때 계산하기,,)
//        LocalDate lastWateredDate = plantCheckRepositorySupport.findLastWateredDateByPlantId(plantId);
//        // 2. plant_type에서 물 주기 가져오기
//        // 3. 마지막으로 물 준 날 + 그 주기 가져와서 더하기 -> plant 테이블의 waterDate 값 채우기
//    }
//
//    @Override
//    public void calculateNextDateToFertilize(Long plantId) {
//
//    }
//
//    @Override
//    public void calculateNextDateToRepot(Long plantId) {
//
//    }
}