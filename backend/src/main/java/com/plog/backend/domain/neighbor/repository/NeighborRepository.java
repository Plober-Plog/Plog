package com.plog.backend.domain.neighbor.repository;

import com.google.common.base.Optional;
import com.plog.backend.domain.neighbor.entity.Neighbor;
import com.plog.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeighborRepository extends JpaRepository<Neighbor, Long> {
    Optional<Neighbor> findByNeighborFromAndNeighborToAndNeighborType(User neighborFrom, User neighborTo, int neighborType);
}
