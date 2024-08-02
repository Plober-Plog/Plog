package com.plog.backend.domain.plant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OtherPlantType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long otherPlantTypeId;
    @Column
    String plantName;
}
