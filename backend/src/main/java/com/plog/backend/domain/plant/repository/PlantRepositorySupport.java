package com.plog.backend.domain.plant.repository;

import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.entity.QPlant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlantRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    private final int size = 15;

    public PlantRepositorySupport(JPAQueryFactory queryFactory) {
        super(Plant.class);
        this.queryFactory = queryFactory;
    }

    public List<Plant> findByUserSearchId(String searchId, int page) {
        QPlant plant = QPlant.plant;

        return queryFactory.selectFrom(plant)
                .where(plant.user.searchId.eq(searchId))
                .orderBy(plant.isFixed.desc(), plant.fixedAt.desc(), plant.createdAt.desc())
                .offset(page * size)
                .limit(size)
                .fetch();
    }

    public List<Plant> findByUserSearchIdAndPlantTypeId(String searchId, Long plantTypeId, int page) {
        QPlant plant = QPlant.plant;

        return queryFactory.selectFrom(plant)
                .where(plant.user.searchId.eq(searchId)
                        .and(plant.plantType.plantTypeId.eq(plantTypeId)))
                .orderBy(plant.isFixed.desc(), plant.fixedAt.desc(), plant.createdAt.desc())
                .offset(page * size)
                .limit(size)
                .fetch();
    }

    public List<Plant> findByUserSearchIdAndOtherPlantTypeId(String searchId, Long otherPlantTypeId, int page) {
        QPlant plant = QPlant.plant;

        return queryFactory.selectFrom(plant)
                .where(plant.user.searchId.eq(searchId)
                        .and(plant.otherPlantType.otherPlantTypeId.eq(otherPlantTypeId)))
                .orderBy(plant.isFixed.desc(), plant.fixedAt.desc(), plant.createdAt.desc())
                .offset(page * size)
                .limit(size)
                .fetch();
    }
}
