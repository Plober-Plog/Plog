package com.plog.backend.domain.diary.service;

import com.plog.backend.domain.diary.dto.request.PlantDiaryAddRequestDto;
import com.plog.backend.domain.diary.dto.request.PlantDiaryUpdateRequestDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryGetResponseDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryGetSimpleResponseDto;
import com.plog.backend.domain.diary.dto.response.PlantDiaryWeatherGetResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlantDiaryService {
    PlantDiaryWeatherGetResponseDto getWeatherData(String token, String date);

    void uploadPlantDiaryImages(MultipartFile[] images, int thumbnailIdx, Long plantDiaryId);

    Long addPlantDiary(String token, PlantDiaryAddRequestDto plantDiaryAddRequestDto);

    void updatePlantDiary(String token, PlantDiaryUpdateRequestDto plantDiaryUpdateRequestDto);

    void deletePlantDiary(String token, Long plantDiaryId);

    PlantDiaryGetResponseDto getPlantDiary(Long plantDiaryId);

    PlantDiaryGetResponseDto getPlantDiaryByRecordDate(Long plantId, String recordDate);

    List<PlantDiaryGetSimpleResponseDto> getPlantDiaryByYearAndMonth(Long plantId, String year, String month);

    List<PlantDiaryGetSimpleResponseDto> getPlantDiaryRecentFive(Long plantId);
}