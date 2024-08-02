package com.plog.backend.domain.user.repository;

import com.plog.backend.domain.user.entity.QUser;
import com.plog.backend.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositorySupport extends QuerydslRepositorySupport {
    private JPAQueryFactory queryFactory = null;

    public User findByUserId(Long userId) {
        QUser user = QUser.user;
        return queryFactory.selectFrom(user)
                .where(user.userId.eq(userId))
                .fetchOne();
    }

    public UserRepositorySupport(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }

    public User findByEmail(String email) {
        QUser user = QUser.user;
        return queryFactory.selectFrom(user)
                .where(user.email.eq(email))
                .fetchOne();
    }

    public User findBySearchId(String searchId) {
        QUser user = QUser.user;
        return queryFactory.selectFrom(user)
                .where(user.searchId.eq(searchId))
                .fetchOne();
    }

}
