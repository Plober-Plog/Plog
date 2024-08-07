package com.plog.backend.domain.neighbor.repository;

import com.plog.backend.domain.neighbor.entity.QNeighbor;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class NeighborRepositorySupport extends QuerydslRepositorySupport {
    private JPAQueryFactory queryFactory = null;

    public NeighborRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(NeighborRepository.class);
        this.queryFactory = jpaQueryFactory;
    }

    public int findNeighborTypeByNeighborToAndNeighborFrom(Long neighborTo, Long neighborFrom) {
        QNeighbor qNeighbor = QNeighbor.neighbor;
        return Objects.requireNonNull(queryFactory.selectFrom(qNeighbor)
                .where(qNeighbor.neighborTo.userId.eq(neighborTo).and(qNeighbor.neighborFrom.userId.eq(neighborFrom)))
                .fetchOne()).getNeighborType().getValue();

    }
}
