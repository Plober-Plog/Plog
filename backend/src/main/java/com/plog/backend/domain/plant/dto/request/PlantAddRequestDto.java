package com.plog.backend.domain.plant.dto.request;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantAddRequestDto {
    Long plantTypeId;
    Long otherPlantTypeId;
    String profile;
    String bio;
    String nickname;
    Date birthDate;
    boolean hasNotified;
    boolean isFixed;
}