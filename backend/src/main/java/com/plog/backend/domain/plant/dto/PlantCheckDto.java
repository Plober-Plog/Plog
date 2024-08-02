package com.plog.backend.domain.plant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantCheckDto {
    @JsonProperty("isWatered")
    boolean isWatered;
    @JsonProperty("isFertilized")
    boolean isFertilized;
    @JsonProperty("isRepotted")
    boolean isRepotted;
    Date checkDate;
}
