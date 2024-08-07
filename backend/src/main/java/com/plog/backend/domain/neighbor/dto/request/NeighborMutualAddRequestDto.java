package com.plog.backend.domain.neighbor.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class NeighborMutualAddRequestDto {
    private String neighborSearchId;
    private Boolean isDelete;
}
