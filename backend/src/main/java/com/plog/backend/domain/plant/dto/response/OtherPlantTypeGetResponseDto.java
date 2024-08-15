package com.plog.backend.domain.plant.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OtherPlantTypeGetResponseDto {
    private Long otherPlantTypeId;
    private String plantName;
}
