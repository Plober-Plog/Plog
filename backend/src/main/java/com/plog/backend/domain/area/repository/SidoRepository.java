package com.plog.backend.domain.area.repository;

import com.plog.backend.domain.area.entity.Sido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SidoRepository extends JpaRepository<Sido, Long> {
}
