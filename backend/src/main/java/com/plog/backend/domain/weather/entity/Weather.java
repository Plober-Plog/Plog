package com.plog.backend.domain.weather.entity;

import com.plog.backend.domain.area.entity.Gugun;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "weather")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDate date;
    @Column
    private Double avgTemp;
    @Column
    private Double avgHumidity;
    @Column
    private int weather;

    @ManyToOne
    @JoinColumn(name = "gugun_id")
    private Gugun gugun;
}
