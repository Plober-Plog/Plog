package com.plog.backend.domain.neighbor.repository;

import com.plog.backend.domain.neighbor.entity.Neighbor;
import com.plog.backend.domain.neighbor.entity.QNeighbor;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class NeighborRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public NeighborRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Neighbor.class);
        this.queryFactory = jpaQueryFactory;
    }

    public Integer findNeighborTypeByNeighborToAndNeighborFrom(Long neighborTo, Long neighborFrom) {
        QNeighbor qNeighbor = QNeighbor.neighbor;
        Neighbor neighbor = queryFactory.selectFrom(qNeighbor)
                .where(qNeighbor.neighborTo.userId.eq(neighborTo)
                        .and(qNeighbor.neighborFrom.userId.eq(neighborFrom)))
                .fetchOne();

        if (neighbor != null && neighbor.getNeighborType() != null) {
            return neighbor.getNeighborType().getValue();
        } else {
            return null; // 예외를 던지거나 기본 값을 반환
        }
    }
}
