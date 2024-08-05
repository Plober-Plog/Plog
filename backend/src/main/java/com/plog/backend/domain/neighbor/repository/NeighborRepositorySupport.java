package com.plog.backend.domain.neighbor.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class NeighborRepositorySupport extends QuerydslRepositorySupport {
    private JPAQueryFactory queryFactory = null;

    public NeighborRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(NeighborRepository.class);
        this.queryFactory = jpaQueryFactory;
    }

}
