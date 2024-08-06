package com.plog.backend.domain.image.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plant_diary_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlantDiaryImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plantDiaryImageId;

    @Column(nullable = false)
    private Long plantDiaryId;

    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @Column(name = "`order`", nullable = false)
    private int order;

    @Column(nullable = false)
    private boolean isThumbnail = false;

}
