package com.plog.backend.domain.plant.dto.request;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlantCheckRequestDto {
    Date checkDate;
}
