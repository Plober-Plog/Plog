package com.plog.backend.domain.diary.service;

import com.plog.backend.domain.diary.dto.request.PlantDiaryAddRequestDto;
import com.plog.backend.domain.diary.dto.request.PlantDiaryUpdateRequestDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryGetResponseDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryGetSimpleResponseDto;
import com.plog.backend.domain.diary.entity.Humidity;
import com.plog.backend.domain.diary.entity.PlantDiary;
import com.plog.backend.domain.diary.entity.Weather;
import com.plog.backend.domain.diary.repository.PlantDiaryRepository;
import com.plog.backend.domain.plant.dto.response.PlantGetResponseDto;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.repository.PlantRepository;
import com.plog.backend.domain.plant.service.PlantService;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotAuthorizedRequestException;
import com.plog.backend.global.exception.NotValidRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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


    @Override
    public void addPlantDiary(String token, PlantDiaryAddRequestDto plantDiaryAddRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> addPlantDiary - 요청 데이터: {}", plantDiaryAddRequestDto);

        LocalDate recordDate = plantDiaryAddRequestDto.getRecordDate();
        if (recordDate.isAfter(LocalDate.now())) {
            throw new NotValidRequestException("미래의 식물 일지는 작성할 수 없습니다");
        }

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
            plantDiaryRepository.save(plantDiary);
            log.info(">>> addPlantDiary - 일지 저장 완료: {}", plantDiary);
        } else {
            throw new NotValidRequestException("일지를 작성할 식물이 없습니다.");
        }
    }

    @Transactional
    @Override
    public void updatePlantDiary(String token, Long plantDiaryId, PlantDiaryUpdateRequestDto plantDiaryUpdateRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> updatePlantDiary - 요청 ID: {}, 업데이트 데이터: {}", plantDiaryId, plantDiaryUpdateRequestDto);

        LocalDate recordDate = plantDiaryUpdateRequestDto.getRecordDate();
        if (recordDate.isAfter(LocalDate.now())) {
            throw new NotValidRequestException("미래의 식물 일지는 작성할 수 없습니다");
        }

        Optional<PlantDiary> plantDiary = plantDiaryRepository.findById(plantDiaryId);
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
                log.info(">>> updatePlantDiary - 일지 수정 완료: {}", pd);
            } else {
                throw new EntityNotFoundException("일치하는 식물을 찾을 수 없습니다.");
            }
        } else {
            throw new EntityNotFoundException("일치하는 식물 일지를 찾을 수 없습니다.");
        }
    }

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
                return PlantDiaryGetResponseDto.builder()
                        .plantId(plantDiary.getPlant().getPlantId())
                        .weather(plantDiary.getWeather().getValue())
                        .humidity(plantDiary.getHumidity().getValue())
                        .temperature(plantDiary.getTemperature())
                        .content(plantDiary.getContent())
                        .recordDate(plantDiary.getRecordDate())
                        .build();
            } else {
                throw new NotValidRequestException("삭제된 일지입니다.");
            }
        } else {
            throw new EntityNotFoundException("일치하는 일지가 없습니다.");
        }
    }

    @Override
    public List<PlantDiaryGetSimpleResponseDto> getPlantDiaryByRecordDate(Long plantId, String recordDate) {
        List<PlantDiary> plantDiaryList = plantDiaryRepository.findAllByPlantPlantIdAndRecordDate(plantId, LocalDate.parse(recordDate));
        List<PlantDiaryGetSimpleResponseDto> plantDiaryGetSimpleResponseDtoList = new ArrayList<>();
        for (PlantDiary plantDiary : plantDiaryList) {
            plantDiaryGetSimpleResponseDtoList.add(new PlantDiaryGetSimpleResponseDto(plantDiary.getPlantDiaryId(), plantDiary.getRecordDate()));
        }
        return plantDiaryGetSimpleResponseDtoList;
    }

    @Override
    public List<PlantDiaryGetSimpleResponseDto> getPlantDiaryByYearAndMonth(Long plantId, String year, String month) {
        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);
        LocalDate startDate = LocalDate.of(yearInt, monthInt, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<PlantDiary> plantDiaryList = plantDiaryRepository.findAllByPlantPlantIdAndRecordDateBetween(plantId, startDate, endDate);
        List<PlantDiaryGetSimpleResponseDto> plantDiaryGetSimpleResponseDtoList = new ArrayList<>();
        for (PlantDiary plantDiary : plantDiaryList) {
            plantDiaryGetSimpleResponseDtoList.add(new PlantDiaryGetSimpleResponseDto(plantDiary.getPlantDiaryId(), plantDiary.getRecordDate()));
        }
        return plantDiaryGetSimpleResponseDtoList;
    }

    @Override
    public List<PlantDiaryGetSimpleResponseDto> getPlantDiaryRecentFive(Long plantId) {
        List<PlantDiary> plantDiaryList = plantDiaryRepository.findTop5ByPlantPlantIdOrderByRecordDateDesc(plantId);
        List<PlantDiaryGetSimpleResponseDto> plantDiaryGetSimpleResponseDtoList = new ArrayList<>();
        for (PlantDiary plantDiary : plantDiaryList) {
            plantDiaryGetSimpleResponseDtoList.add(new PlantDiaryGetSimpleResponseDto(plantDiary.getPlantDiaryId(), plantDiary.getRecordDate()));
        }
        return plantDiaryGetSimpleResponseDtoList;
    }
}
