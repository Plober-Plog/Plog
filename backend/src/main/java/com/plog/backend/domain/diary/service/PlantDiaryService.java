package com.plog.backend.domain.diary.service;

import com.plog.backend.domain.diary.dto.request.PlantDiaryAddRequestDto;
import com.plog.backend.domain.diary.dto.request.PlantDiaryUpdateRequestDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryGetResponseDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryGetSimpleResponseDto;

import java.util.List;

public interface PlantDiaryService {
    void addPlantDiary(String token, PlantDiaryAddRequestDto plantDiaryAddRequestDto);
    void updatePlantDiary(String token, Long plantDiaryId,PlantDiaryUpdateRequestDto plantDiaryUpdateRequestDto);
    void deletePlantDiary(String token, Long plantDiaryId);
    PlantDiaryGetResponseDto getPlantDiary(Long plantDiaryId);

    List<PlantDiaryGetSimpleResponseDto> getPlantDiaryByRecordDate(Long plantId, String recordDate);

    List<PlantDiaryGetSimpleResponseDto> getPlantDiaryByYearAndMonth(Long plantId, String year, String month);

    List<PlantDiaryGetSimpleResponseDto> getPlantDiaryRecentFive(Long plantId);
}