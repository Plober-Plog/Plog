package com.plog.realtime.domain.image.repository;

import com.plog.realtime.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByImageUrlAndIsDeletedFalse(String imageUrl);
}

