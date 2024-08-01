package com.plog.backend.domain.plant.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlant is a Querydsl query type for Plant
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlant extends EntityPathBase<Plant> {

    private static final long serialVersionUID = -1411833818L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlant plant = new QPlant("plant");

    public final StringPath bio = createString("bio");

    public final DatePath<java.time.LocalDate> birthDate = createDate("birthDate", java.time.LocalDate.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DatePath<java.time.LocalDate> deadDate = createDate("deadDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> fertilizeDate = createDate("fertilizeDate", java.time.LocalDate.class);

    public final NumberPath<Integer> fixed = createNumber("fixed", Integer.class);

    public final BooleanPath hasNotified = createBoolean("hasNotified");

    public final com.plog.backend.domain.image.entity.QImage image;

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final DateTimePath<java.time.LocalDateTime> modifiedAt = createDateTime("modifiedAt", java.time.LocalDateTime.class);

    public final StringPath nickname = createString("nickname");

    public final QOtherPlantType otherPlantType;

    public final NumberPath<Integer> plantId = createNumber("plantId", Integer.class);

    public final QPlantType plantType;

    public final DatePath<java.time.LocalDate> repotDate = createDate("repotDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> waterDate = createDate("waterDate", java.time.LocalDate.class);

    public QPlant(String variable) {
        this(Plant.class, forVariable(variable), INITS);
    }

    public QPlant(Path<? extends Plant> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlant(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlant(PathMetadata metadata, PathInits inits) {
        this(Plant.class, metadata, inits);
    }

    public QPlant(Class<? extends Plant> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.image = inits.isInitialized("image") ? new com.plog.backend.domain.image.entity.QImage(forProperty("image")) : null;
        this.otherPlantType = inits.isInitialized("otherPlantType") ? new QOtherPlantType(forProperty("otherPlantType")) : null;
        this.plantType = inits.isInitialized("plantType") ? new QPlantType(forProperty("plantType")) : null;
    }

}

