package com.plog.backend.domain.neighbor.repository;

import com.plog.backend.domain.neighbor.entity.Neighbor;
import com.plog.backend.domain.user.entity.User;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeighborRepository extends JpaRepository<Neighbor, Long> {
    Optional<Neighbor> findByNeighborFromAndNeighborToAndNeighborType(User neighborFrom, User neighborTo, int neighborType);
    List<Neighbor> findByNeighborToAndNeighborType(User neighborTo, int neighborType);
    List<Neighbor> findByNeighborFrom(User neighborFrom);
    List<Neighbor> findByNeighborTo(User neighborTo);
    int countByNeighborFrom(User neighborFrom);
    int countByNeighborTo(User neighborTo);
}
