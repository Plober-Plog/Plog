package com.plog.backend.domain.plant.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantTypeGetResponse {
    String plantName;
    String profile;
    String guide;
    int waterInterval;
    int fertilizeInterval;
    int repotInterval;
}
