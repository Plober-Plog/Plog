package com.plog.backend.domain.plant.repository;

import com.plog.backend.domain.plant.dto.response.OtherPlantTypeGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetSimpleResponseDto;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.entity.QOtherPlantType;
import com.plog.backend.domain.plant.entity.QPlant;
import com.plog.backend.domain.plant.entity.QPlantType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlantRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    private final int size = 15;
    private final int validValue = 2;

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

    public List<PlantTypeGetSimpleResponseDto> findDistinctPlantTypeIdtByUserSearchId(String searchId) {
        QPlant plant = QPlant.plant;
        QPlantType plantType = QPlantType.plantType;
        return queryFactory.select(Projections.constructor(PlantTypeGetSimpleResponseDto.class,
                        plant.plantType.plantTypeId,
                        plantType.plantName))
                .distinct()
                .from(plant)
                .join(plant.plantType, plantType)
                .where(plant.user.searchId.eq(searchId)
                        .and(plant.plantType.plantTypeId.goe(validValue)))
                .fetch();
    }

    public List<OtherPlantTypeGetResponseDto> findDistinctOtherPlantTypeIdByUserSearchId(String searchId) {
        QPlant plant = QPlant.plant;
        QOtherPlantType otherPlantType = QOtherPlantType.otherPlantType;

        return queryFactory.select(Projections.constructor(OtherPlantTypeGetResponseDto.class,
                        plant.otherPlantType.otherPlantTypeId,
                        otherPlantType.plantName))
                .distinct()
                .from(plant)
                .where(plant.user.searchId.eq(searchId)
                        .and(plant.otherPlantType.otherPlantTypeId.goe(validValue)))
                .fetch();
    }
}
