package com.plog.backend.domain.report.service;

import com.plog.backend.domain.diary.entity.PlantDiary;
import com.plog.backend.domain.diary.repository.PlantDiaryRepository;
import com.plog.backend.domain.image.entity.PlantDiaryImage;
import com.plog.backend.domain.image.repository.ImageRepository;
import com.plog.backend.domain.image.repository.PlantDiaryImageRepository;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.entity.PlantCheck;
import com.plog.backend.domain.plant.entity.PlantType;
import com.plog.backend.domain.plant.repository.PlantCheckRepository;
import com.plog.backend.domain.plant.repository.PlantRepository;
import com.plog.backend.domain.report.dto.response.ReportResultResponseDto;
import com.plog.backend.domain.report.entity.ReportResult;
import com.plog.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service("ReportService")
public class ReportServiceImpl implements ReportService {

    private final PlantDiaryRepository plantDiaryRepository;
    private final PlantCheckRepository plantCheckRepository;
    private final PlantRepository plantRepository;
    private final PlantDiaryImageRepository plantDiaryImageRepository;
    private final ImageRepository imageRepository;

    @Override
    public ReportResultResponseDto createReport(Long plantId) {
//    public ReportResultResponseDto createReport(Long plantId, ReportCreateRequestDto reportCreateRequestDto) {
//        LocalDate startDate = reportCreateRequestDto.getStartDate();
//        LocalDate endDate = reportCreateRequestDto.getEndDate();

        // 식물 일지 아이디 기준으로 특정 기간의 데이터를 가져옴
//        List<PlantDiary> plantDiaries = plantDiaryRepository.findPlantDiariesByPlantPlantIdAndRecordDateBetween(plantId, startDate, endDate);
        List<PlantDiary> plantDiaries = plantDiaryRepository.findPlantDiariesByPlantPlantId(plantId);

        if (plantDiaries.isEmpty()) {
            throw new EntityNotFoundException(">>> 없는 식물 일지 입니다.");
        }

        Plant plant = plantRepository.findById(plantId).orElseThrow(
                ()->{
                    return new EntityNotFoundException("없는 식물 입니다.");
                }
        );
        PlantType plantType = plant.getPlantType();
        log.info(">>> Plant: " + plant + ", PlantType: " + plantType);

        LocalDate startDate = LocalDate.from(plantDiaries.get(0).getRecordDate());
        LocalDate endDate = LocalDate.from(plantDiaries.get(plantDiaries.size() - 1).getRecordDate());

        log.info(">>> Report - startDate: " + startDate + ", endDate: " + endDate);

//        String firstDayImageUrl = plantDiaryImageRepository
//                .findByPlantDiaryPlantDiaryIdAndImageIsDeletedFalseOrderByOrderAsc(
//                        plantDiaries.get(0).getPlantDiaryId())
//                .get(0).getImage().getImageUrl();
        String firstDayImageUrl = plantDiaries.get(0).getPlant().getImage().getImageUrl();
//        String recentImageUrl = plantDiaries.get(plantDiaries.size() - 1).getPlant().getImage().getImageUrl(); // 제일 최근 사진
        Optional<PlantDiaryImage> optionalImage = plantDiaryImageRepository.findByPlantDiaryPlantDiaryIdAndIsThumbnailTrue(
                plantDiaries.get(plantDiaries.size() - 1).getPlantDiaryId());

        if (optionalImage.isPresent()) {
            String recentImageUrl = optionalImage.get().getImage().getImageUrl();
            log.info("최근 이미지 URL: " + recentImageUrl);
        } else {
            log.error(">>> 최근 이미지 URL을 찾을 수 없습니다.");
            throw new EntityNotFoundException("최근 이미지 URL을 찾을 수 없습니다.");
        }

        String recentImageUrl = optionalImage.get().getImage().getImageUrl();

        log.info("첫 번째 날 이미지 URL: " + firstDayImageUrl);
        log.info("최근 이미지 URL: " + recentImageUrl);

        // 특정 기간의 plantCheck 데이터를 가져옴
        List<PlantCheck> plantChecks = plantCheckRepository.findPlantChecksByPlantPlantIdAndCheckDateBetween(plant.getPlantId(), startDate, endDate);
        log.info(">>> PlantChecks: " + plantChecks);
        
        log.info(">>> Start Date: " + startDate + ", End Date: " + endDate);


        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        log.info(">>> 키운 일 수: " + daysBetween);

        // 가이드 데이터
        int guideWater = (int)(daysBetween / plantType.getWaterInterval());
        int guideFertilize = (int)(daysBetween / plantType.getFertilizeInterval());
        int guideRepot = (int)(daysBetween / plantType.getRepotInterval());
        log.info(">>> Guide Water: " + guideWater + ", Guide Fertilize: " + guideFertilize + ", Guide Repot: " + guideRepot);

        // 식물 데이터
        int water = 0;
        int fertilize = 0;
        int repot = 0;

        for (PlantCheck plantCheck : plantChecks) {
            water += plantCheck.isWatered() ? 1 : 0;
            fertilize += plantCheck.isFertilized() ? 1 : 0;
            repot += plantCheck.isRepotted() ? 1 : 0;
        }
        log.info(">>> Water: " + water + ", Fertilize: " + fertilize + ", Repot: " + repot);

        // 만약 한번도 안했다면?
        water = water != 0 ? water : Integer.MAX_VALUE;
        fertilize = fertilize != 0 ? fertilize : Integer.MAX_VALUE;
        repot = repot != 0 ? repot : Integer.MAX_VALUE;

        // 두 개의 값을 뺄 때, 0에 가까우면 좋음. feat. 윤서 알고리즘 아이디어에 착안했습니다.
        // abs(guide - data)로 큰 값일수록 안 좋음. rating을 1~4(최고, 성장중, 아쉽다, 살려줘)
        int waterRating = Math.abs(guideWater - water);
        int fertilizeRating = Math.abs(guideFertilize - fertilize);
        int repoRating = Math.abs(guideRepot - repot);
        log.info(">>> Water Rating: " + waterRating + ", Fertilize Rating: " + fertilizeRating + ", Repo Rating: " + repoRating);



        ReportResult waterResult = ReportResult.getReportResult(waterRating);
        ReportResult fertilizeResult = ReportResult.getReportResult(fertilizeRating);
        ReportResult repoResult = ReportResult.getReportResult(repoRating);

        log.info(">>> 물 주기 결과: " + waterResult);
        log.info(">>> 영양분 주기 결과: " + fertilizeResult);
        log.info(">>> 분갈이 주기 결과: " + repoResult);

        water = water == Integer.MAX_VALUE?0:water;
        fertilize = fertilize == Integer.MAX_VALUE?0:fertilize;
        repot = repot == Integer.MAX_VALUE?0:repot;

        ReportResultResponseDto responseDto = ReportResultResponseDto.builder()
                .plantName(plant.getNickname())
                .firstDayImageUrl(firstDayImageUrl)
                .recentImageUrl(recentImageUrl)
                .waterResult(resultToString(waterResult.getValue()))
                .fertilizeResult(resultToString(fertilizeResult.getValue()))
                .repoResult(resultToString(repoResult.getValue()))
                .waterData(water)
                .fertilizeData(fertilize)
                .repotData(repot)
                .build();

        log.info(">>> createReport - ReportResultResponseDto: " + responseDto);

        return responseDto;
    }

    public String resultToString(int i) {
        switch (i) {
            case 1:
                return "최고에요!";
            case 2:
                return "잘 성장중이에요!";
            case 3:
                return "많은 관심이 필요해요!";
            default:
                return "사..살려줘..";
        }
    }
}
