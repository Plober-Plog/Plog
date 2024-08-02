package com.plog.backend.domain.diary.entity;

import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.global.model.dto.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class PlantDiary extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plantDiaryId;

    @ManyToOne
    @JoinColumn(name = "plant_id", referencedColumnName = "plantId")
    private Plant plant;

    @Column
    private Weather weather;
    @Column
    private float temperature;
    @Column
    private Humidity humidity;

    @Column
    private String content;
    @Column
    private LocalDate recordDate;

    @Column
    private float width;
    @Column
    private float height;
    @Column
    private boolean canAnalyze;
}
