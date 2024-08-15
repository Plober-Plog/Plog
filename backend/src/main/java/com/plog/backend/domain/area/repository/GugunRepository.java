package com.plog.backend.domain.area.repository;

import com.plog.backend.domain.area.entity.Gugun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GugunRepository extends JpaRepository<Gugun, Long> {
    List<Gugun> findBySidoSidoCode(int sidoCode);
    Optional<Gugun> findBySidoSidoCodeAndGugunCode(int sidoCode, int gugunCode);
}
