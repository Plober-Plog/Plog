package com.plog.backend.domain.plant.dto.request;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantCheckRequestDto {
    boolean isWatered;
    boolean isFertilized;
    boolean isRepotted;
    Date checkDate;
}
