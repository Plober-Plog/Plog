package com.plog.backend.domain.plant.entity;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.global.model.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "plant")
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Plant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plantId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "imageId")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "plant_type_id", referencedColumnName = "plantTypeId")
    private PlantType plantType;

    @ManyToOne
    @JoinColumn(name = "other_plant_type_id", referencedColumnName = "otherPlantTypeId")
    private OtherPlantType otherPlantType;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String bio;

    @Column
    private LocalDate birthDate;

    @Column
    private LocalDate deadDate;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean hasNotified;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isFixed;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;

    @Column
    private LocalDate waterDate;

    @Column
    private LocalDate fertilizeDate;

    @Column
    private LocalDate repotDate;

    @Column
    private LocalDateTime fixedAt;

    @Builder
    public Plant(User user, PlantType plantType, OtherPlantType otherPlantType,
                 String nickname, Image image, String bio, LocalDate birthDate) {
        this.user = user;
        this.plantType = plantType;
        this.otherPlantType = otherPlantType;
        this.nickname = nickname;
        this.image = image;
        this.bio = bio;
        this.birthDate = birthDate;
    }
}
