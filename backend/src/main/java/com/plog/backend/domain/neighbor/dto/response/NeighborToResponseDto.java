package com.plog.backend.domain.neighbor.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NeighborToResponseDto {
    private String searchId;
    private String profile;
    private String nickname;
    private int neighborType;
}