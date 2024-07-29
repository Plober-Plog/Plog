package com.plog.backend.domain.plant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlantAddRequest {
    int plantTypeId;
    int otherPlantTypeId;
    String profile;
    String nickname;
    LocalDateTime birthDate;
    boolean hasNotified;
    boolean isFixed;
}