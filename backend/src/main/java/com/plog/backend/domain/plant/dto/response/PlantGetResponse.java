package com.plog.backend.domain.plant.dto.response;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantGetResponse {
    Long plantTypeId;
    Long otherPlantId;
    String nickname;
    String profile;
    Date birthDate;
    boolean hasNotified;
    int fixed;
}
