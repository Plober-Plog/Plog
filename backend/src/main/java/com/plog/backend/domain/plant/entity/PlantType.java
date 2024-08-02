package com.plog.backend.domain.plant.entity;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.global.model.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
public class PlantType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long plantTypeId;
    @Column
    String plantName;
    @Column
    String guide;
    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "imageId")
    Image image;

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