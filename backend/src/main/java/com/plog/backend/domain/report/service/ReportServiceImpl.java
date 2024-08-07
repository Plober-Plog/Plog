package com.plog.backend.domain.report.service;

import com.plog.backend.domain.diary.entity.PlantDiary;
import com.plog.backend.domain.diary.repository.PlantDiaryRepository;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.entity.PlantCheck;
import com.plog.backend.domain.plant.entity.PlantType;
import com.plog.backend.domain.plant.repository.PlantCheckRepository;
import com.plog.backend.domain.plant.repository.PlantTypeRepository;
import com.plog.backend.domain.report.dto.request.ReportCreateRequestDto;
import com.plog.backend.domain.report.dto.response.ReportResultResponseDto;
import com.plog.backend.domain.report.entity.ReportResult;
import com.plog.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("ReportService")
public class ReportServiceImpl implements ReportService {

    private final PlantDiaryRepository plantDiaryRepository;
    private final PlantCheckRepository plantCheckRepository;

    @Override
    public ReportResultResponseDto createReport(Long plantDiaryId, ReportCreateRequestDto reportCreateRequestDto) {
        LocalDate startDate = reportCreateRequestDto.getStartDate();
        LocalDate endDate = reportCreateRequestDto.getEndDate();

        // 식물 일지 아이디 기준으로 특정 기간의 데이터를 가져옴
        List<PlantDiary> plantDiary = plantDiaryRepository.findPlantDiariesByPlantPlantIdAndRecordDateBetween(plantDiaryId, startDate, endDate);

        if (plantDiary.size() == 0) {
            throw new EntityNotFoundException(">>> 없는 식물 일지 입니다.");
        }

        Plant plant = plantDiary.get(0).getPlant();
        PlantType plantType = plant.getPlantType();
        log.info(">>> Plant: " + plant + ", PlantType: " + plantType);

        String firstDayImageUrl = plant.getImage().getImageUrl();
        String recentImageUrl = plantDiary.get(plantDiary.size() - 1).getPlant().getImage().getImageUrl(); // 제일 최근 사진
        log.info("첫 번째 날 이미지 URL: " + firstDayImageUrl);
        log.info("최근 이미지 URL: " + recentImageUrl);

        // 특정 기간의 plantCheck 데이터를 가져옴
        List<PlantCheck> plantChecks = plantCheckRepository.findPlantChecksByPlantPlantIdAndCheckDateBetween(plant.getPlantId(), startDate, endDate);
        log.info("PlantChecks: " + plantChecks);
        
        log.info("Start Date: " + startDate + ", End Date: " + endDate);


        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        log.info("키운 일 수: " + daysBetween);

        // 가이드 데이터
        int guideWater = (int)(daysBetween / plantType.getWaterInterval());
        int guideFertilize = (int)(daysBetween / plantType.getFertilizeInterval());
        int guideRepot = (int)(daysBetween / plantType.getRepotInterval());
        log.info("Guide Water: " + guideWater + ", Guide Fertilize: " + guideFertilize + ", Guide Repot: " + guideRepot);

        // 식물 데이터
        int water = 0;
        int fertilize = 0;
        int repot = 0;

        for (PlantCheck plantCheck : plantChecks) {
            water += plantCheck.isWatered() ? 1 : 0;
            fertilize += plantCheck.isFertilized() ? 1 : 0;
            repot += plantCheck.isRepotted() ? 1 : 0;
        }
        log.info("Water: " + water + ", Fertilize: " + fertilize + ", Repot: " + repot);

        // 두 개의 값을 뺄 때, 0에 가까우면 좋음.
        // abs(guide - data)로 큰 값일수록 안 좋음. rating을 1~4(최고, 성장중, 아쉽다, 살려줘)
        int waterRating = Math.abs(guideWater - water);
        int fertilizeRating = Math.abs(guideFertilize - fertilize);
        int repoRating = Math.abs(guideRepot - repot);
        log.info("Water Rating: " + waterRating + ", Fertilize Rating: " + fertilizeRating + ", Repo Rating: " + repoRating);

        ReportResult waterResult = ReportResult.getReportResult(waterRating);
        ReportResult fertilizeResult = ReportResult.getReportResult(fertilizeRating);
        ReportResult repoResult = ReportResult.getReportResult(repoRating);

        log.info("Water Result: " + waterResult);
        log.info("Fertilize Result: " + fertilizeResult);
        log.info("Repo Result: " + repoResult);

        ReportResultResponseDto responseDto = ReportResultResponseDto.builder()
                .plantName(plant.getNickname())
                .firstDayImageUrl(firstDayImageUrl)
                .recentImageUrl(recentImageUrl)
                .waterResult(waterResult.getValue())
                .fertilizeResult(fertilizeResult.getValue())
                .repoResult(repoResult.getValue())
                .waterData(water)
                .fertilizeData(fertilize)
                .repotData(repot)
                .build();

        log.info("ReportResultResponseDto: " + responseDto);

        return responseDto;
    }
}
