package com.plog.realtime.domain.plant.repository;

import com.plog.realtime.domain.plant.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    Optional<Plant> findByNicknameAndIsDeletedFalseAndDeadDateIsNull(String nickname);
}
