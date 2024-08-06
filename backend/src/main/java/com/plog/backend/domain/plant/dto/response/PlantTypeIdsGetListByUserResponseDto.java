package com.plog.backend.domain.plant.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantTypeIdsGetListByUserResponseDto {
    List<PlantTypeGetSimpleResponseDto> plantTypes;
    List<OtherPlantTypeGetResponseDto> otherPlantTypes;
}
