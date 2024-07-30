package com.plog.backend.domain.plant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class OtherPlantType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int otherPlantTypeId;
    @Column
    String plantName;
}
