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
    private String plantName;
    private String guide;
    private MultipartFile[] image;

    private int waterInterval;
    private int repotInterval;
    private int fertilizeInterval;

    private int repotMid;
    private int waterMid;
    private int fertilizeMid;
}
