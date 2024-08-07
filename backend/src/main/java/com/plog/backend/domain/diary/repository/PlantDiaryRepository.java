package com.plog.backend.domain.diary.repository;

import com.plog.backend.domain.diary.entity.PlantDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlantDiaryRepository extends JpaRepository<PlantDiary, Long> {
    PlantDiary
    findByPlantPlantIdAndRecordDate(Long plantId, LocalDate recordDate);

    List<PlantDiary> findAllByPlantPlantIdAndRecordDateBetween(Long plantId, LocalDate startDate, LocalDate endDate);

    List<PlantDiary> findTop5ByPlantPlantIdOrderByRecordDateDesc(Long plantId);

    List<PlantDiary> findPlantDiariesByPlantPlantIdAndRecordDateBetween(Long plantDiaryId, LocalDate startDate, LocalDate endDate);

    boolean findByPlantDiaryId(Long plantDiaryId);
}
