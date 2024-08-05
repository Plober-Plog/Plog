package com.plog.backend.domain.neighbor.repository;

import com.plog.backend.domain.neighbor.entity.Neighbor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NeighborRepository extends JpaRepository<Neighbor, Long> {
}
