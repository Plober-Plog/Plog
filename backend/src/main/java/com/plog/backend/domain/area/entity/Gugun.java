package com.plog.backend.domain.area.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gugun")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Gugun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gugun_id")
    Long gugunId;

    @Column(name = "gugun_code", nullable = false)
    int gugunCode;

    @Column(name = "gugun_name")
    String gugunName;

    @ManyToOne
    @JoinColumn(name = "sido_code", referencedColumnName = "sido_code", nullable = false)
    Sido sido;

    @Builder
    public Gugun(Long gugunId, int gugunCode, String gugunName, Sido sido) {
        this.gugunId = gugunId;
        this.gugunCode = gugunCode;
        this.gugunName = gugunName;
        this.sido = sido;
    }
}