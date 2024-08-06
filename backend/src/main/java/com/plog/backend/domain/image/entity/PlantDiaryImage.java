package com.plog.backend.domain.image.entity;

import com.plog.backend.domain.diary.entity.PlantDiary;
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

    @ManyToOne
    @JoinColumn(name = "plant_diary_id", nullable = false)
    private PlantDiary plantDiary;

    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @Column(name = "`order`", nullable = false)
    private int order; // 1부터 시작

    @Column(nullable = false)
    private boolean isThumbnail = false;

}
