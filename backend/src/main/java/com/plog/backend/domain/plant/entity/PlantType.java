package com.plog.backend.domain.plant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlantType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int plantTypeId;
    @Column(nullable = false)
    String plantName;
    @Column
    String guide;

    @Column
    int waterInterval;
    @Column
    int repotInterval;
    @Column
    int fertilizeInterval;

    @Column
    int repotMid;
    @Column
    int waterMid;
    @Column
    int fertilizeMid;
}