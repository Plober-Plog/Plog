package com.plog.backend.domain.plant.entity;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.global.model.dto.BaseEntity;
import com.plog.backend.global.util.DateUtil;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Plant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long plantId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    User user;

    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "imageId")
    Image image;

    @ManyToOne
    @JoinColumn(name = "plant_type_id", referencedColumnName = "plantTypeId")
    PlantType plantType;

    @ManyToOne
    @JoinColumn(name = "other_plant_type_id", referencedColumnName = "otherPlantTypeId")
    OtherPlantType otherPlantType;

    @Column(nullable = false)
    String nickname;

    @Column
    String bio;

    @Column(nullable = false)
    LocalDate birthDate;

    @Column
    LocalDate deadDate;

    @Column(nullable = false)
    @ColumnDefault("true")
    boolean hasNotified;

    @Column(nullable = false)
    @ColumnDefault("255")
    int fixed;

    @Column(nullable = false)
    @ColumnDefault("false")
    boolean isDeleted;

    @Column
    LocalDate waterDate;

    @Column
    LocalDate fertilizeDate;

    @Column
    LocalDate repotDate;

    @Builder
    public Plant(PlantType plantType, OtherPlantType otherPlantType, String nickname, Image image, Date birthDate, boolean hasNotified, boolean isFixed) {
        this.plantType = plantType;
        this.otherPlantType = otherPlantType;
        this.nickname = nickname;
        this.image = image;
        this.birthDate = DateUtil.getInstance().convertToLocalDate(birthDate);
        this.hasNotified = hasNotified;
        this.fixed = isFixed ? 1 : 255; //TODO [강윤서] - fixed 계산
    }
}
