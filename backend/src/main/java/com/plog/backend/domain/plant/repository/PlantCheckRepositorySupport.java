package com.plog.backend.domain.plant.repository;

import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.entity.QPlantCheck;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class PlantCheckRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public PlantCheckRepositorySupport(JPAQueryFactory queryFactory) {
        super(Plant.class);
        this.queryFactory = queryFactory;
    }

    public LocalDate findLastWateredDateByPlantId(Long plantId) {
        QPlantCheck plantCheck = QPlantCheck.plantCheck;
        return queryFactory.select(plantCheck.checkDate)
                .from(plantCheck)
                .where(plantCheck.plant.plantId.eq(plantId)
                        .and(plantCheck.isWatered.isTrue()))
                .orderBy(plantCheck.checkDate.desc())
                .fetchFirst();
    }

    public LocalDate findLastFertilizedDateByPlantId(Long plantId) {
        QPlantCheck plantCheck = QPlantCheck.plantCheck;
        return queryFactory.select(plantCheck.checkDate)
                .from(plantCheck)
                .where(plantCheck.plant.plantId.eq(plantId)
                        .and(plantCheck.isFertilized.isTrue()))
                .orderBy(plantCheck.checkDate.desc())
                .fetchFirst();
    }

    public LocalDate findLastRepottedDateByPlantId(Long plantId) {
        QPlantCheck plantCheck = QPlantCheck.plantCheck;
        return queryFactory.select(plantCheck.checkDate)
                .from(plantCheck)
                .where(plantCheck.plant.plantId.eq(plantId)
                        .and(plantCheck.isRepotted.isTrue()))
                .orderBy(plantCheck.checkDate.desc())
                .fetchFirst();
    }
}
