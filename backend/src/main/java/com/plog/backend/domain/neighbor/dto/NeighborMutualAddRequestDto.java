package com.plog.backend.domain.neighbor.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class NeighborMutualAddRequestDto {
    private String neighborSearchId;
    private boolean isDelete;
}
