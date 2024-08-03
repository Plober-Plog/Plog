package com.plog.backend.domain.diary.entity;

import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.global.model.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "plant_diary")
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
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

    @Column
    @ColumnDefault("false")
    private boolean isDeleted;

    @Builder
    PlantDiary(Plant plant, int weather, float temperature, int humidity, String content, LocalDate recordDate) {
        this.plant = plant;
        this.weather = Weather.weather(weather);
        this.temperature = temperature;
        this.humidity = Humidity.humidity(humidity);
        this.content = content;
        this.recordDate = recordDate;
    }
}
