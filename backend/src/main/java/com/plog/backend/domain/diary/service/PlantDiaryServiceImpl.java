package com.plog.backend.domain.diary.service;

import com.plog.backend.domain.diary.dto.request.PlantDiaryAddRequestDto;
import com.plog.backend.domain.diary.dto.request.PlantDiaryImageUploadRequestDto;
import com.plog.backend.domain.diary.dto.request.PlantDiaryUpdateRequestDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryGetResponseDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryGetSimpleResponseDto;
import com.plog.backend.domain.diary.entity.Humidity;
import com.plog.backend.domain.diary.entity.PlantDiary;
import com.plog.backend.domain.diary.entity.Weather;
import com.plog.backend.domain.diary.repository.PlantDiaryRepository;
import com.plog.backend.domain.image.entity.PlantDiaryImage;
import com.plog.backend.domain.image.repository.ImageRepository;
import com.plog.backend.domain.image.repository.PlantDiaryImageRepository;
import com.plog.backend.domain.image.service.ImageServiceImpl;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.repository.PlantRepository;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotAuthorizedRequestException;
import com.plog.backend.global.exception.NotValidRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.plog.backend.global.util.JwtTokenUtil.jwtTokenUtil;

@Slf4j
@RequiredArgsConstructor
@Service("plantDiaryService")
public class PlantDiaryServiceImpl implements PlantDiaryService {

    private final PlantDiaryRepository plantDiaryRepository;
    private final PlantRepository plantRepository;
    private final ImageServiceImpl imageService;
    private final ImageRepository imageRepository;
    private final PlantDiaryImageRepository plantDiaryImageRepository;

    @Transactional
    public void uploadPlantDiaryImages(List<PlantDiaryImageUploadRequestDto> plantDiaryImageUploadRequestDtoList, Long plantDiaryId) {
        // 대표 사진 찾기
        int thumbnailIdx = -1;
        for (int i = 0; i < plantDiaryImageUploadRequestDtoList.size(); i++) {
            if (plantDiaryImageUploadRequestDtoList.get(i).isThumbnail()) {
                if (thumbnailIdx == -1) {
                    thumbnailIdx = i;
                } else {
                    throw new NotValidRequestException("일지에는 대표 사진을 한 장만 지정할 수 있습니다.");
                }
            }
        }

        // 이미지 업로드
        MultipartFile[] images = new MultipartFile[plantDiaryImageUploadRequestDtoList.size()];
        String[] imageUrls = imageService.plantDiaryUploadImages(images, plantDiaryId, thumbnailIdx);
    }

    @Transactional
    @Override
    public void addPlantDiary(String token, PlantDiaryAddRequestDto plantDiaryAddRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> addPlantDiary - 요청 데이터: {}", plantDiaryAddRequestDto);

        LocalDate recordDate = plantDiaryAddRequestDto.getRecordDate();
        if (recordDate.isAfter(LocalDate.now())) {
            throw new NotValidRequestException("미래의 식물 일지는 작성할 수 없습니다");
        }

        if (plantDiaryAddRequestDto.getImages().size() > 5)
            throw new NotValidRequestException("일지에는 최대 5개 사진을 업로드 할 수 있습니다.");

        Optional<Plant> plant = plantRepository.findById(plantDiaryAddRequestDto.getPlantId());
        if (plant.isPresent()) {
            if (userId != plant.get().getUser().getUserId())
                throw new NotAuthorizedRequestException();
            PlantDiary plantDiary = PlantDiary.builder()
                    .plant(plant.get())
                    .weather(plantDiaryAddRequestDto.getWeather())
                    .temperature(plantDiaryAddRequestDto.getTemperature())
                    .humidity(plantDiaryAddRequestDto.getHumidity())
                    .content(plantDiaryAddRequestDto.getContent())
                    .recordDate(recordDate)
                    .build();
            PlantDiary pd = plantDiaryRepository.save(plantDiary);

            // 일지 사진 업로드
            uploadPlantDiaryImages(plantDiaryAddRequestDto.getImages(), pd.getPlantDiaryId());
            log.info(">>> addPlantDiary - 일지 저장 완료: {}", plantDiary);

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
        if (recordDate.isAfter(LocalDate.now())) {
            throw new NotValidRequestException("미래의 식물 일지는 작성할 수 없습니다");
        }

        Optional<PlantDiary> plantDiary = plantDiaryRepository.findById(plantDiaryUpdateRequestDto.getPlantDiaryId());
        if (plantDiary.isPresent()) {
            Optional<Plant> plant = plantRepository.findById(plantDiaryUpdateRequestDto.getPlantId());
            if (plant.isPresent()) {
                if (userId != plant.get().getUser().getUserId())
                    throw new NotAuthorizedRequestException();
                PlantDiary pd = plantDiary.get();
                pd.setPlant(plant.get());
                pd.setContent(plantDiaryUpdateRequestDto.getContent());
                pd.setWeather(Weather.weather(plantDiaryUpdateRequestDto.getWeather()));
                pd.setHumidity(Humidity.humidity(plantDiaryUpdateRequestDto.getHumidity()));
                pd.setRecordDate(recordDate);
                pd.setTemperature(plantDiaryUpdateRequestDto.getTemperature());
                plantDiaryRepository.save(pd);

                //TODO [강윤서]
                // plantDiaryId 의 thumbnail 값을 초기화하고
                // request 속 thumbnailIdx 로 접근하여 true 로 변경
//                List<PlantDiaryImage> plantDiaryImageUrlList = imageService.loadImagesByPlantDiaryId(plantDiaryUpdateRequestDto.getPlantDiaryId());
//                imageService.
                log.info(">>> updatePlantDiary - 일지 수정 완료: {}", pd);
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
                List<PlantDiaryImage> plantDiaryImageList = plantDiaryImageRepository.findByPlantDiaryIdAndImageIsDeletedFalseOrderByOrderAsc(plantDiaryId);
                for (PlantDiaryImage plantDiaryImage : plantDiaryImageList) {
                    imageService.deleteImage(plantDiaryImage.getImage().getImageUrl());
                }
                pd.setDeleted(true);

                plantDiaryRepository.save(pd);

                log.info(">>> deletePlantDiary - 일지 삭제 완료: {}", pd);
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

        Optional<PlantDiary> optionalPlantDiary = plantDiaryRepository.findById(plantDiaryId);
        if (optionalPlantDiary.isPresent()) {
            PlantDiary plantDiary = optionalPlantDiary.get();
            if (!plantDiary.isDeleted()) {
                log.info(">>> getPlantDiary - 일지 조회 성공: {}", plantDiary);
                List<String> imageList = imageService.loadImagesByPlantDiaryId(plantDiaryId);
                return PlantDiaryGetResponseDto.builder()
                        .plantDiaryId(plantDiaryId)
                        .plantId(plantDiary.getPlant().getPlantId())
                        .weather(plantDiary.getWeather().getValue())
                        .humidity(plantDiary.getHumidity().getValue())
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
        PlantDiary plantDiary = plantDiaryRepository.findByPlantPlantIdAndRecordDate(plantId, LocalDate.parse(recordDate));
        log.info(">>> getPlantDiaryByRecordDate 조회 완료 : {}", plantDiary);
        if (plantDiary == null) return null;

        List<String> imageList = imageService.loadImagesByPlantDiaryId(plantDiary.getPlantDiaryId());
        return PlantDiaryGetResponseDto.builder()
                .plantDiaryId(plantDiary.getPlantDiaryId())
                .plantId(plantDiary.getPlant().getPlantId())
                .weather(plantDiary.getWeather().getValue())
                .humidity(plantDiary.getHumidity().getValue())
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

        List<PlantDiary> plantDiaryList = plantDiaryRepository.findAllByPlantPlantIdAndRecordDateBetween(plantId, startDate, endDate);
        log.info(">>> getPlantDiaryByYearAndMonth 조회 완료 : {}", plantDiaryList);
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
        List<PlantDiary> plantDiaryList = plantDiaryRepository.findTop5ByPlantPlantIdOrderByRecordDateDesc(plantId);
        log.info(">>> getPlantDiaryRecentFive 조회 완료 : {}", plantDiaryList);
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
