package com.plog.backend.domain.area.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sido")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Sido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sido_id")
    Long sidoId;

    @Column(name = "sido_code", unique = true, nullable = false)
    int sidoCode;

    @Column(name = "sido_name")
    String sidoName;

    @Builder
    public Sido(Long sidoId, int sidoCode, String sidoName) {
        this.sidoId = sidoId;
        this.sidoCode = sidoCode;
        this.sidoName = sidoName;
    }
}