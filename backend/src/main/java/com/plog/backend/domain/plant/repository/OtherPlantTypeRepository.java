package com.plog.backend.domain.plant.repository;

import com.plog.backend.domain.plant.entity.OtherPlantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtherPlantTypeRepository extends JpaRepository<OtherPlantType, Long> {
}