package com.plog.backend.domain.plant.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class PlantTypeAddRequestDto {
    @Column
    private String plantName;
    @Column
    private String guide;
    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "imageId")
    private MultipartFile[] image;

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
