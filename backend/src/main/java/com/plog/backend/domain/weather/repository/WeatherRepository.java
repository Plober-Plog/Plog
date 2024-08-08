package com.plog.backend.domain.weather.repository;

import com.plog.backend.domain.area.entity.Gugun;
import com.plog.backend.domain.weather.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    boolean existsByDateAndGugun(LocalDate date, Gugun gugun);
    Optional<Weather> findByDateAndGugun(LocalDate date, Gugun gugun);
}
