package com.plog.backend.domain.plant.entity;

import com.plog.backend.global.model.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
public class PlantCheck extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plantCheckId;

    @ManyToOne
    @JoinColumn(name = "plant_id", referencedColumnName = "plantId")
    private Plant plant;

    @Column
    private boolean isWatered;

    @Column
    private boolean isFertilized;

    @Column
    private boolean isRepotted;

    @Column
    private LocalDate checkDate;


}
