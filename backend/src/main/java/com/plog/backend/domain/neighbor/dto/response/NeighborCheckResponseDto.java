package com.plog.backend.domain.neighbor.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class NeighborCheckResponseDto {
    private int to;
    private int from;
}
