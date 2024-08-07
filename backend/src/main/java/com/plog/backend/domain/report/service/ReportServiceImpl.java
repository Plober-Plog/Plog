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
        // 식물 일지 아이디 기준
        List<PlantDiary> plantDiary = plantDiaryRepository.findTop5ByPlantPlantIdOrderByRecordDateDesc(plantDiaryId);

        Plant plant = plantDiary.get(0).getPlant();
        PlantType plantType = plant.getPlantType();

        String firstDayImageUrl = plant.getImage().getImageUrl();
        String recentImageUrl = plantDiary.get(plantDiary.size() - 1).getPlant().getImage().getImageUrl(); // 제일 최근 사진

        List<PlantCheck> plantChecks = plantCheckRepository.findPlantChecksByPlantPlantId(plant.getPlantId());

//        LocalDate startDate = plantChecks.get(0).getCheckDate();
//        LocalDate endDate = plantChecks.get(plantChecks.size() - 1).getCheckDate();
        LocalDate startDate = reportCreateRequestDto.getStartDate();
        LocalDate endDate = reportCreateRequestDto.getEndDate();

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        log.info("키운 일 수: " + daysBetween);

        // 가이드 데이터
        int guideWater = (int)(daysBetween / plantType.getWaterInterval());
        int guideFertilize = (int)(daysBetween / plantType.getFertilizeInterval());
        int guideRepot = (int)(plantType.getRepotInterval());

        // 식물 데이터
        int water = 0;
        int fertilize = 0;
        int repot = 0;

        for (PlantCheck plantCheck : plantChecks) {
            water += plantCheck.isWatered() ? 1 : 0;
            fertilize += plantCheck.isFertilized() ? 1 : 0;
            repot += plantCheck.isRepotted() ? 1 : 0;
        }

        // 두 개의 값을 뺄 때, 0에 가까우면 좋음.
        // abs(guide - data)로 큰 값일수록 안 좋음. rating을 1~4(최고, 성장중, 아쉽다, 살려줘)
        int waterRating = Math.abs(guideWater - water);
        int fertilizeRating = Math.abs(guideFertilize - fertilize);
        int repoRating = Math.abs(guideRepot - repot);

        ReportResult waterResult = ReportResult.getReportResult(waterRating);
        ReportResult fertilizeResult = ReportResult.getReportResult(fertilizeRating);
        ReportResult repoResult = ReportResult.getReportResult(repoRating);

        log.info("Water Rating: " + waterResult);
        log.info("Fertilize Rating: " + fertilizeResult);
        log.info("Repo Rating: " + repoResult);

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

        return responseDto;
    }
}
