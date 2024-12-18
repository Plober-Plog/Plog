package com.plog.backend.domain.plant.entity;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.global.model.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "plant_type")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString
public class PlantType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plantTypeId;
    @Column
    private String plantName;
    @Column
    private String guide;
    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "imageId")
    private Image image;

    @Column
    private int waterInterval;
    @Column
    private int repotInterval;
    @Column
    private int fertilizeInterval;

    @Column
    private int repotMid;
    @Column
    private int waterMid;
    @Column
    private int fertilizeMid;
}