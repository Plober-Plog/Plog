package com.plog.backend.domain.plant.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOtherPlantType is a Querydsl query type for OtherPlantType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOtherPlantType extends EntityPathBase<OtherPlantType> {

    private static final long serialVersionUID = -1566031942L;

    public static final QOtherPlantType otherPlantType = new QOtherPlantType("otherPlantType");

    public final NumberPath<Integer> otherPlantTypeId = createNumber("otherPlantTypeId", Integer.class);

    public final StringPath plantName = createString("plantName");

    public QOtherPlantType(String variable) {
        super(OtherPlantType.class, forVariable(variable));
    }

    public QOtherPlantType(Path<? extends OtherPlantType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOtherPlantType(PathMetadata metadata) {
        super(OtherPlantType.class, metadata);
    }

}

