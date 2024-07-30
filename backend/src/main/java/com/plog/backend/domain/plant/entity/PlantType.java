package com.plog.backend.domain.plant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class PlantType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int plantTypeId;
    @Column
    String guide;
    @Column
    String plantName;

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
