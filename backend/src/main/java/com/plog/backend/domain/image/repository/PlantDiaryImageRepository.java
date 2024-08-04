package com.plog.backend.domain.image.repository;

import com.plog.backend.domain.image.entity.PlantDiaryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantDiaryImageRepository extends JpaRepository<PlantDiaryImage, Long> {
    List<PlantDiaryImage> findByPlantDiaryIdAndImageIsDeletedFalseOrderByOrderAsc(int plantDiaryId);

    Optional<PlantDiaryImage> findByPlantDiaryIdAndIsThumbnailTrue(int plantDiaryId);
}
