package com.plog.backend.domain.diary.service;

import com.plog.backend.domain.area.entity.Gugun;
import com.plog.backend.domain.area.repository.GugunRepository;
import com.plog.backend.domain.diary.dto.request.PlantDiaryAddRequestDto;
import com.plog.backend.domain.diary.dto.request.PlantDiaryUpdateRequestDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryGetResponseDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryGetSimpleResponseDto;
import com.plog.backend.domain.diary.entity.Humidity;
import com.plog.backend.domain.diary.entity.PlantDiary;
import com.plog.backend.domain.diary.entity.Weather;
import com.plog.backend.domain.diary.repository.PlantDiaryRepository;
import com.plog.backend.domain.image.dto.PlantDiaryImageGetResponseDto;
import com.plog.backend.domain.image.entity.PlantDiaryImage;
import com.plog.backend.domain.image.exception.ImageNotFoundException;
import com.plog.backend.domain.image.repository.PlantDiaryImageRepository;
import com.plog.backend.domain.image.service.ImageServiceImpl;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.repository.PlantRepository;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static com.plog.backend.global.util.JwtTokenUtil.jwtTokenUtil;

@Slf4j
@RequiredArgsConstructor
@Service("plantDiaryService")
public class PlantDiaryServiceImpl implements PlantDiaryService {

    private final PlantDiaryRepository plantDiaryRepository;
    private final PlantRepository plantRepository;
    private final ImageServiceImpl imageService;
    private final PlantDiaryImageRepository plantDiaryImageRepository;
    private final UserRepository userRepository;
    private final GugunRepository gugunRepository;

    @Override
    public void getWeatherData(String token, String date) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("일치하는 회원을 찾을 수 없습니다."));
        LocalDate recordDate = LocalDate.parse(date); // 일지를 작성할 날짜
        Gugun gugun = gugunRepository.findBySidoSidoCodeAndGugunCode(
                user.getSidoCode(), user.getGugunCode()
        ).orElseThrow(() -> new EntityNotFoundException("일치하는 위치 정보를 조회할 수 없습니다."));
        String keyValue = "weather:" + recordDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ":" + gugun.getGugunId();
        log.info("Weather Data 키: {}", keyValue);
    }

    @Transactional
    public void uploadPlantDiaryImages(MultipartFile[] images, int thumbnailIdx, Long plantDiaryId) {
        if (images.length > 5)
            throw new NotValidRequestException("일지에는 최대 5개 사진을 업로드 할 수 있습니다.");

        // 이미지 업로드
        imageService.plantDiaryUploadImages(images, plantDiaryId, thumbnailIdx);
    }

    @Transactional
    @Override
    public Long addPlantDiary(String token, PlantDiaryAddRequestDto plantDiaryAddRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> addPlantDiary - 요청 데이터: {}", plantDiaryAddRequestDto);

        LocalDate recordDate = plantDiaryAddRequestDto.getRecordDate();
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        if (recordDate.isAfter(currentDate)) {
            throw new NotValidRequestException("미래의 식물 일지는 작성할 수 없습니다");
        }

        Optional<Plant> plant = plantRepository.findById(plantDiaryAddRequestDto.getPlantId());
        if (plant.isPresent()) {
            if (userId != plant.get().getUser().getUserId())
                throw new NotAuthorizedRequestException();
            PlantDiary existPlantDiary = plantDiaryRepository.findByPlantPlantIdAndRecordDateAndIsDeletedFalse(
                    plantDiaryAddRequestDto.getPlantId(), plantDiaryAddRequestDto.getRecordDate());
            if (existPlantDiary != null)
                throw new NotValidRequestException("이미 해당 일자에 작성된 일지가 존재합니다.");
            PlantDiary plantDiary = plantDiaryRepository.save(PlantDiary.builder()
                    .plant(plant.get())
                    .weather(plantDiaryAddRequestDto.getWeather())
                    .temperature(plantDiaryAddRequestDto.getTemperature())
                    .humidity(plantDiaryAddRequestDto.getHumidity())
                    .content(plantDiaryAddRequestDto.getContent())
                    .recordDate(recordDate)
                    .build());

            log.info(">>> addPlantDiary - 일지 저장 완료 : 일지 id {}", plantDiary.getPlantDiaryId());
            return plantDiary.getPlantDiaryId();
        } else {
            throw new NotValidRequestException("일지를 작성할 식물이 없습니다.");
        }
    }

    @Transactional
    @Override
    public void updatePlantDiary(String token, PlantDiaryUpdateRequestDto plantDiaryUpdateRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> updatePlantDiary - 요청 ID: {}, 업데이트 데이터: {}", plantDiaryUpdateRequestDto.getPlantDiaryId(), plantDiaryUpdateRequestDto);

        LocalDate recordDate = plantDiaryUpdateRequestDto.getRecordDate();
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        if (recordDate.isAfter(currentDate)) {
            throw new NotValidRequestException("미래의 식물 일지는 작성할 수 없습니다");
        }

        Optional<PlantDiary> plantDiary = plantDiaryRepository.findById(plantDiaryUpdateRequestDto.getPlantDiaryId());
        if (plantDiary.isPresent()) {
            Optional<Plant> plant = plantRepository.findById(plantDiaryUpdateRequestDto.getPlantId());
            if (plant.isPresent()) {
                if (userId != plant.get().getUser().getUserId())
                    throw new NotAuthorizedRequestException();

                PlantDiary pd = plantDiary.get();

                // 일지 내용 수정
                pd.setPlant(plant.get());
                pd.setContent(plantDiaryUpdateRequestDto.getContent());
                pd.setWeather(Weather.weather(plantDiaryUpdateRequestDto.getWeather()));
                pd.setHumidity(Humidity.humidity(plantDiaryUpdateRequestDto.getHumidity()));
                pd.setRecordDate(recordDate);
                pd.setTemperature(plantDiaryUpdateRequestDto.getTemperature());
                plantDiaryRepository.save(pd);

                log.info(">>> updatePlantDiary - 일지 내용 수정 완료: {}", pd.getPlantDiaryId());
                // 일지 대표 사진 수정
                Optional<PlantDiaryImage> plantDiaryImageOptional = plantDiaryImageRepository.findByPlantDiaryPlantDiaryIdAndIsThumbnailTrue(plantDiary.get().getPlantDiaryId());
                if (plantDiaryImageOptional.isPresent()) { // 등록되어 있으면 기존 썸네일 해제 진행
                    PlantDiaryImage plantDiaryImage = plantDiaryImageOptional.get();
                    plantDiaryImage.setThumbnail(false);
                    plantDiaryImageRepository.save(plantDiaryImage);
                    log.info(">>> updatePlantDiary - 기존 썸네일 해제 완료");

                    // 새로 들어온 thumbnailIdx 에 해당하는 order 이미지의 isThumbnail 을 true로 update
                    int thumbnailIdx = plantDiaryUpdateRequestDto.getThumbnailIdx();
                    List<PlantDiaryImageGetResponseDto> plantDiaryImageGetResponseDtoList = imageService.loadImagesByPlantDiaryId(plantDiaryUpdateRequestDto.getPlantDiaryId());
                    if (thumbnailIdx >= 0
                            && thumbnailIdx < plantDiaryImageGetResponseDtoList.size()) {
                        PlantDiaryImageGetResponseDto newThumbnailPlantDiaryImage = plantDiaryImageGetResponseDtoList.get(thumbnailIdx);
                        PlantDiaryImage newThumbnailImage = PlantDiaryImage.builder()
                                .plantDiaryImageId(newThumbnailPlantDiaryImage.getPlantDiaryImageId())
                                .plantDiary(pd)
                                .order(newThumbnailPlantDiaryImage.getOrder())
                                .isThumbnail(true) // 새로 썸네일로 지정
                                .image(newThumbnailPlantDiaryImage.getImage())
                                .build();
                        plantDiaryImageRepository.save(newThumbnailImage);
                        log.info(">>> updatePlantDiary - 새로운 일지 썸네일 지정 완료");
                    } else {
                        log.info("thumbnailIdx가 유효한 값이 아닙니다.");
                    }
                } else {
                    log.info("해당 일지에는 사진이 등록되어 있지 않습니다.");
                }
            } else {
                throw new EntityNotFoundException("일치하는 식물을 찾을 수 없습니다.");
            }
        } else {
            throw new EntityNotFoundException("일치하는 식물 일지를 찾을 수 없습니다.");
        }
    }

    @Transactional
    @Override
    public void deletePlantDiary(String token, Long plantDiaryId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> deletePlantDiary - 요청 ID: {}", plantDiaryId);

        Optional<PlantDiary> plantDiary = plantDiaryRepository.findById(plantDiaryId);
        if (plantDiary.isPresent()) {
            PlantDiary pd = plantDiary.get();
            if (pd.isDeleted()) {
                throw new NotValidRequestException("이미 삭제된 일지입니다.");
            }
            Optional<Plant> plant = plantRepository.findById(pd.getPlant().getPlantId());
            if (plant.isPresent()) {
                if (userId != plant.get().getUser().getUserId())
                    throw new NotAuthorizedRequestException();
                List<PlantDiaryImage> plantDiaryImageList = plantDiaryImageRepository.findByPlantDiaryPlantDiaryIdAndImageIsDeletedFalseOrderByOrderAsc(plantDiaryId);
                for (PlantDiaryImage plantDiaryImage : plantDiaryImageList) {
                    imageService.deleteImage(plantDiaryImage.getImage().getImageUrl());
                }
                pd.setDeleted(true);

                plantDiaryRepository.save(pd);

                log.info(">>> deletePlantDiary - 일지 삭제 완료: {}", pd.getPlantDiaryId());
            } else {
                throw new EntityNotFoundException("일치하는 식물을 찾을 수 없습니다.");
            }
        } else {
            throw new EntityNotFoundException("일치하는 식물 일지를 찾을 수 없습니다.");
        }
    }

    @Override
    public PlantDiaryGetResponseDto getPlantDiary(Long plantDiaryId) {
        log.info(">>> getPlantDiary - 요청 ID: {}", plantDiaryId);

        Optional<PlantDiary> optionalPlantDiary = plantDiaryRepository.findByPlantDiaryIdAndIsDeletedFalse(plantDiaryId);
        if (optionalPlantDiary.isPresent()) {
            PlantDiary plantDiary = optionalPlantDiary.get();
            if (!plantDiary.isDeleted()) {
                log.info(">>> getPlantDiary - 일지 조회 성공: {}", plantDiary.getPlantDiaryId());
                List<String> imageList = imageService.loadImageUrlsByPlantDiaryId(plantDiaryId);
                return PlantDiaryGetResponseDto.builder()
                        .plantDiaryId(plantDiaryId)
                        .plantId(plantDiary.getPlant().getPlantId())
                        .weather(Weather.weather(plantDiary.getWeather().getValue()))
                        .humidity(Humidity.humidity(plantDiary.getHumidity().getValue()))
                        .temperature(plantDiary.getTemperature())
                        .content(plantDiary.getContent())
                        .recordDate(plantDiary.getRecordDate())
                        .images(imageList)
                        .build();
            } else {
                throw new NotValidRequestException("삭제된 일지입니다.");
            }
        } else {
            throw new EntityNotFoundException("일치하는 일지가 없습니다.");
        }
    }

    @Override
    public PlantDiaryGetResponseDto getPlantDiaryByRecordDate(Long plantId, String recordDate) {
        PlantDiary plantDiary = plantDiaryRepository.findByPlantPlantIdAndRecordDateAndIsDeletedFalse(plantId, LocalDate.parse(recordDate));
        log.info(">>> getPlantDiaryByRecordDate 조회 완료 ");
        if (plantDiary == null) return null;

        List<String> imageList = imageService.loadImageUrlsByPlantDiaryId(plantDiary.getPlantDiaryId());
        return PlantDiaryGetResponseDto.builder()
                .plantDiaryId(plantDiary.getPlantDiaryId())
                .plantId(plantDiary.getPlant().getPlantId())
                .weather(Weather.weather(plantDiary.getWeather().getValue()))
                .humidity(Humidity.humidity(plantDiary.getHumidity().getValue()))
                .temperature(plantDiary.getTemperature())
                .content(plantDiary.getContent())
                .recordDate(plantDiary.getRecordDate())
                .images(imageList)
                .build();
    }

    @Override
    public List<PlantDiaryGetSimpleResponseDto> getPlantDiaryByYearAndMonth(Long plantId, String year, String month) {
        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);
        LocalDate startDate = LocalDate.of(yearInt, monthInt, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<PlantDiary> plantDiaryList = plantDiaryRepository.findAllByPlantPlantIdAndIsDeletedFalseAndRecordDateBetween(plantId, startDate, endDate);
        log.info(">>> getPlantDiaryByYearAndMonth 조회 완료");
        List<PlantDiaryGetSimpleResponseDto> plantDiaryGetSimpleResponseDtoList = new ArrayList<>();
        for (PlantDiary plantDiary : plantDiaryList) {
            String thumbnailUrl = imageService.loadThumbnailImageByPlantDiaryId(plantDiary.getPlantDiaryId());
            plantDiaryGetSimpleResponseDtoList.add(
                    new PlantDiaryGetSimpleResponseDto(
                            plantDiary.getPlantDiaryId(),
                            plantDiary.getRecordDate(),
                            thumbnailUrl
                    )
            );
        }
        return plantDiaryGetSimpleResponseDtoList;
    }

    @Override
    public List<PlantDiaryGetSimpleResponseDto> getPlantDiaryRecentFive(Long plantId) {
        List<PlantDiary> plantDiaryList = plantDiaryRepository.findTop5ByPlantPlantIdAndIsDeletedFalseOrderByRecordDateDesc(plantId);
        log.info(">>> getPlantDiaryRecentFive 조회 완료");
        List<PlantDiaryGetSimpleResponseDto> plantDiaryGetSimpleResponseDtoList = new ArrayList<>();
        for (PlantDiary plantDiary : plantDiaryList) {
            String thumbnailUrl = imageService.loadThumbnailImageByPlantDiaryId(plantDiary.getPlantDiaryId());
            plantDiaryGetSimpleResponseDtoList.add(
                    new PlantDiaryGetSimpleResponseDto(
                            plantDiary.getPlantDiaryId(),
                            plantDiary.getRecordDate(),
                            thumbnailUrl
                    ));
        }
        return plantDiaryGetSimpleResponseDtoList;
    }
}
