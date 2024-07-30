package com.plog.backend.domain.plant.repository;

import com.plog.backend.domain.plant.entity.Plant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PlantRepositorySupport {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private PlantRepository plantRepository;

    public Plant addPlant(Plant plant) {
        return plantRepository.save(plant);
    }
}
