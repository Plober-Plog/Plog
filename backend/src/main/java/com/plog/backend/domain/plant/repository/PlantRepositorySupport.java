package com.plog.backend.domain.plant.repository;

import com.plog.backend.domain.plant.dto.response.OtherPlantTypeGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantGetResponseDto;
import com.plog.backend.domain.plant.dto.response.PlantTypeGetSimpleResponseDto;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.entity.QOtherPlantType;
import com.plog.backend.domain.plant.entity.QPlant;
import com.plog.backend.domain.plant.entity.QPlantType;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PlantRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    private final int size = 15;
    private final int validValue = 2;

    public PlantRepositorySupport(JPAQueryFactory queryFactory) {
        super(Plant.class);
        this.queryFactory = queryFactory;
    }

    public List<PlantGetResponseDto> findByUserSearchId(String searchId, int page) {
        QPlant plant = QPlant.plant;
        QPlantType plantType = QPlantType.plantType;
        QOtherPlantType otherPlantType = QOtherPlantType.otherPlantType;

        List<Tuple> results = queryFactory.select(
                        plant.plantId,
                        plant.plantType.plantTypeId,
                        plantType.plantName,
                        plant.otherPlantType.otherPlantTypeId,
                        otherPlantType.plantName,
                        plant.nickname,
                        plant.bio,
                        plant.image.imageUrl,
                        plant.birthDate,
                        plant.deadDate,
                        plant.notifySetting,
                        plant.isFixed,
                        plant.isDeleted
                )
                .from(plant)
                .leftJoin(plantType).on(plant.plantType.plantTypeId.eq(plantType.plantTypeId))
                .leftJoin(otherPlantType).on(plant.otherPlantType.otherPlantTypeId.eq(otherPlantType.otherPlantTypeId))
                .where(plant.user.searchId.eq(searchId).and(plant.isDeleted.eq(false)))
                .orderBy(plant.isFixed.desc(), plant.fixedAt.desc(), plant.createdAt.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        return results.stream()
                .map(tuple -> PlantGetResponseDto.builder()
                        .plantId(tuple.get(plant.plantId))
                        .plantTypeId(tuple.get(plant.plantType.plantTypeId))
                        .plantTypeName(tuple.get(plantType.plantName))
                        .otherPlantId(tuple.get(plant.otherPlantType.otherPlantTypeId))
                        .otherPlantTypeName(tuple.get(otherPlantType.plantName))
                        .nickname(tuple.get(plant.nickname))
                        .bio(tuple.get(plant.bio))
                        .profile(tuple.get(plant.image.imageUrl))
                        .birthDate(tuple.get(plant.birthDate))
                        .deadDate(tuple.get(plant.deadDate))
                        .hasNotification(tuple.get(plant.notifySetting) == 7 ? true : false)
                        .isFixed(tuple.get(plant.isFixed))
                        .isDeleted(tuple.get(plant.isDeleted))
                        .build()
                )
                .collect(Collectors.toList());
    }


    public List<PlantGetResponseDto> findByUserSearchIdAndPlantTypeId(String searchId, Long plantTypeId, int page) {
        QPlant plant = QPlant.plant;
        QPlantType plantType = QPlantType.plantType;

        List<Tuple> results = queryFactory.select(
                        plant.plantId,
                        plant.plantType.plantTypeId,
                        plantType.plantName,
                        plant.otherPlantType.otherPlantTypeId,
                        plant.nickname,
                        plant.bio,
                        plant.image.imageUrl,
                        plant.birthDate,
                        plant.deadDate,
                        plant.notifySetting,
                        plant.isFixed,
                        plant.isDeleted
                )
                .from(plant)
                .leftJoin(plantType).on(plant.plantType.plantTypeId.eq(plantType.plantTypeId))
                .where(plant.user.searchId.eq(searchId).and(plant.plantType.plantTypeId.eq(plantTypeId)))
                .orderBy(plant.isFixed.desc(), plant.fixedAt.desc(), plant.createdAt.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        return results.stream()
                .map(tuple -> PlantGetResponseDto.builder()
                        .plantId(tuple.get(plant.plantId))
                        .plantTypeId(tuple.get(plant.plantType.plantTypeId))
                        .plantTypeName(tuple.get(plantType.plantName))
                        .otherPlantId(tuple.get(plant.otherPlantType.otherPlantTypeId))
                        .nickname(tuple.get(plant.nickname))
                        .bio(tuple.get(plant.bio))
                        .profile(tuple.get(plant.image.imageUrl))
                        .birthDate(tuple.get(plant.birthDate))
                        .deadDate(tuple.get(plant.deadDate))
                        .hasNotification(tuple.get(plant.notifySetting) == 7 ? true : false)
                        .isFixed(tuple.get(plant.isFixed))
                        .isDeleted(tuple.get(plant.isDeleted))
                        .build()
                )
                .collect(Collectors.toList());
    }

    public List<PlantGetResponseDto> findByUserSearchIdAndOtherPlantTypeId(String searchId, Long otherPlantTypeId, int page) {
        QPlant plant = QPlant.plant;
        QOtherPlantType otherPlantType = QOtherPlantType.otherPlantType;

        List<Tuple> results = queryFactory.select(
                        plant.plantId,
                        plant.plantType.plantTypeId,
                        plant.otherPlantType.otherPlantTypeId,
                        otherPlantType.plantName,
                        plant.nickname,
                        plant.bio,
                        plant.image.imageUrl,
                        plant.birthDate,
                        plant.deadDate,
                        plant.notifySetting,
                        plant.isFixed,
                        plant.isDeleted
                )
                .from(plant)
                .leftJoin(otherPlantType).on(plant.otherPlantType.otherPlantTypeId.eq(otherPlantType.otherPlantTypeId))
                .where(
                        plant.user.searchId.eq(searchId)
                                .and(plant.otherPlantType.otherPlantTypeId.eq(otherPlantTypeId))
                                .and(plant.isDeleted.eq(false))
                )
                .orderBy(plant.isFixed.desc(), plant.fixedAt.desc(), plant.createdAt.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        return results.stream()
                .map(tuple -> PlantGetResponseDto.builder()
                        .plantId(tuple.get(plant.plantId))
                        .plantTypeId(tuple.get(plant.plantType.plantTypeId))
                        .otherPlantId(tuple.get(plant.otherPlantType.otherPlantTypeId))
                        .otherPlantTypeName(tuple.get(otherPlantType.plantName))
                        .nickname(tuple.get(plant.nickname))
                        .bio(tuple.get(plant.bio))
                        .profile(tuple.get(plant.image.imageUrl))
                        .birthDate(tuple.get(plant.birthDate))
                        .deadDate(tuple.get(plant.deadDate))
                        .hasNotification(tuple.get(plant.notifySetting) == 7 ? true : false)
                        .isFixed(tuple.get(plant.isFixed))
                        .isDeleted(tuple.get(plant.isDeleted))
                        .build()
                )
                .collect(Collectors.toList());
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
                        .and(plant.plantType.plantTypeId.goe(validValue))
                        .and(plant.isDeleted.eq(false))
                )
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
                        .and(plant.otherPlantType.otherPlantTypeId.goe(validValue))
                        .and(plant.isDeleted.eq(false))
                )
                .fetch();
    }
}
