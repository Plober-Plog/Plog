package com.plog.backend.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 377921334L;

    public static final QUser user = new QUser("user");

    public final DatePath<java.time.LocalDate> birthDate = createDate("birthDate", java.time.LocalDate.class);

    public final EnumPath<ChatAuth> chatAuth = createEnum("chatAuth", ChatAuth.class);

    public final DatePath<java.time.LocalDate> createAt = createDate("createAt", java.time.LocalDate.class);

    public final StringPath email = createString("email");

    public final EnumPath<Gender> gender = createEnum("gender", Gender.class);

    public final NumberPath<Integer> gugunCode = createNumber("gugunCode", Integer.class);

    public final BooleanPath isAd = createBoolean("isAd");

    public final DatePath<java.time.LocalDate> modifiedAt = createDate("modifiedAt", java.time.LocalDate.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileInfo = createString("profileInfo");

    public final EnumPath<Provider> provider = createEnum("provider", Provider.class);

    public final StringPath provider_id = createString("provider_id");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final StringPath searchId = createString("searchId");

    public final NumberPath<Integer> sidoCode = createNumber("sidoCode", Integer.class);

    public final StringPath source = createString("source");

    public final EnumPath<State> state = createEnum("state", State.class);

    public final NumberPath<Integer> totalExp = createNumber("totalExp", Integer.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

