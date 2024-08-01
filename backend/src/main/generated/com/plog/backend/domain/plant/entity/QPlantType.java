package com.plog.backend.domain.plant.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPlantType is a Querydsl query type for PlantType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlantType extends EntityPathBase<PlantType> {

    private static final long serialVersionUID = -595025792L;

    public static final QPlantType plantType = new QPlantType("plantType");

    public final NumberPath<Integer> fertilizeInterval = createNumber("fertilizeInterval", Integer.class);

    public final NumberPath<Integer> fertilizeMid = createNumber("fertilizeMid", Integer.class);

    public final StringPath guide = createString("guide");

    public final StringPath plantName = createString("plantName");

    public final NumberPath<Integer> plantTypeId = createNumber("plantTypeId", Integer.class);

    public final NumberPath<Integer> repotInterval = createNumber("repotInterval", Integer.class);

    public final NumberPath<Integer> repotMid = createNumber("repotMid", Integer.class);

    public final NumberPath<Integer> waterInterval = createNumber("waterInterval", Integer.class);

    public final NumberPath<Integer> waterMid = createNumber("waterMid", Integer.class);

    public QPlantType(String variable) {
        super(PlantType.class, forVariable(variable));
    }

    public QPlantType(Path<? extends PlantType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPlantType(PathMetadata metadata) {
        super(PlantType.class, metadata);
    }

}

