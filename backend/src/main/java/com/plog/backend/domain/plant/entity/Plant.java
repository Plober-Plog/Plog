package com.plog.backend.domain.plant.entity;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.global.util.DateUtil;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int plantId;

    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "imageId")
    Image image;

    @ManyToOne
    @JoinColumn(name = "plant_type_id", referencedColumnName = "plantTypeId", nullable = true)
    PlantType plantType;

    @ManyToOne
    @JoinColumn(name = "other_plant_type_id", referencedColumnName = "otherPlantTypeId", nullable = true)
    OtherPlantType otherPlantType;

    @Column(nullable = false)
    String nickname;

    @Column
    String bio;

    @Column(nullable = false)
    LocalDate birthDate;

    @Column
    LocalDate deadDate;

    @Column
    @ColumnDefault("true")
    boolean hasNotified;

    @Column
    @ColumnDefault("255")
    int fixed;

    @Column
    @ColumnDefault("false")
    boolean isDeleted;

    @Column
    LocalDate waterDate;

    @Column
    LocalDate fertilizeDate;

    @Column
    LocalDate repotDate;

    @Column(updatable = false)
    @CreatedDate
    LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    LocalDateTime modifiedAt;

    // 기본 식물
    @Builder
    public Plant(PlantType plantType, String nickname, Image image, Date birthDate) {
        this.plantType = plantType;
        this.nickname = nickname;
        this.image = image;
        this.birthDate = DateUtil.getInstance().convertToLocalDate(birthDate);
    }

    // 기타 식물
    @Builder
    public Plant(OtherPlantType otherPlantType, String nickname, Image image, Date birthDate) {
        this.otherPlantType = otherPlantType;
        this.nickname = nickname;
        this.image = image;
        this.birthDate = DateUtil.getInstance().convertToLocalDate(birthDate);
    }
}
