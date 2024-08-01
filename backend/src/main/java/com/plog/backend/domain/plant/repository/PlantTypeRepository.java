package com.plog.backend.domain.plant.repository;

import com.plog.backend.domain.plant.entity.PlantType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantTypeRepository extends JpaRepository<PlantType, Long> {
}