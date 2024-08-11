package com.plog.realtime.domain.plant.entity;

import com.plog.realtime.domain.image.entity.Image;
import com.plog.realtime.global.model.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "plant_type")
@NoArgsConstructor
@Getter
@Setter
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