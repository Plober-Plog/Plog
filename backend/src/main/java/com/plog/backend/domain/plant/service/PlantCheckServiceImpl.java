package com.plog.backend.domain.plant.service;

import com.plog.backend.domain.plant.dto.request.PlantCheckAddRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantCheckUpdateRequestDto;
import com.plog.backend.domain.plant.dto.request.PlantGetByYearAndMonthRequestDto;
import com.plog.backend.domain.plant.dto.response.PlantCheckGetResponseDto;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.entity.PlantCheck;
import com.plog.backend.domain.plant.repository.PlantCheckRepository;
import com.plog.backend.domain.plant.repository.PlantRepository;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotAuthorizedRequestException;
import com.plog.backend.global.exception.NotValidRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.plog.backend.global.util.JwtTokenUtil.jwtTokenUtil;

@Slf4j
@RequiredArgsConstructor
@Service("plantCheckService")
public class PlantCheckServiceImpl implements PlantCheckService {
    private final PlantRepository plantRepository;
    private final PlantCheckRepository plantCheckRepository;

    @Override
    public void addPlantCheck(String token, PlantCheckAddRequestDto plantCheckAddRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        LocalDate recordDate = plantCheckAddRequestDto.getCheckDate();
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        if (recordDate.isAfter(currentDate)) {
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

        LocalDate recordDate = plantCheckUpdateRequestDto.getCheckDate();
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        if (recordDate.isAfter(currentDate)) {
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
            log.info(">>> getPlantCheck - 관리 기록 조회 완료 {}", plantCheck.get().getPlantCheckId());
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

}
