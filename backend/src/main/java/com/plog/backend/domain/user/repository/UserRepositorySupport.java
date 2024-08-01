package com.plog.backend.domain.user.repository;

import com.plog.backend.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositorySupport extends QuerydslRepositorySupport {
    private JPAQueryFactory queryFactory = null;

    public UserRepositorySupport() {
        super(User.class);
        this.queryFactory = queryFactory;
    }

//    public User findBySearchId(String searchId) {
//        return null;
//    }
}
