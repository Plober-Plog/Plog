package com.plog.backend.domain.plant.repository;

import com.plog.backend.domain.plant.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository  extends JpaRepository<Plant, Long> {
}