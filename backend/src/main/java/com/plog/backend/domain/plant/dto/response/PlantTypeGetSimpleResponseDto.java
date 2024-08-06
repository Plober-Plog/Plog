package com.plog.backend.domain.plant.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantTypeGetSimpleResponseDto {
    private Long plantTypeId;
    private String plantName;
}
