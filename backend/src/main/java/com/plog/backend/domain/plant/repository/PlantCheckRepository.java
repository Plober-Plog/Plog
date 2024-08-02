package com.plog.backend.domain.plant.repository;

import com.plog.backend.domain.plant.entity.PlantCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PlantCheckRepository extends JpaRepository<PlantCheck, Long> {
    Optional<PlantCheck> findByPlantPlantIdAndCheckDate(Long plantId, LocalDate checkDate);
}
