package com.plog.backend.domain.neighbor.repository;

import com.plog.backend.domain.neighbor.entity.Neighbor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeighborRepository extends JpaRepository<Neighbor, Long> {
}
