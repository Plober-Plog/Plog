package com.plog.backend.domain.plant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantGetRequestDto {
    @Schema(description = "회원을 식별할 id 값", example = "testid")
    private String searchId;

    @Schema(description = "기본 식물 종류 ID의 리스트 (2부터 유효한 값)", example = "[2, 5]")
    private List<Long> plantTypeId;

    @Schema(description = "기타 식물 종류 ID의 리스트 (2부터 유효한 값)", example = "[]")
    private List<Long> otherPlantTypeId;

    @Schema(description = "목록 조회 슬라이싱을 위한 페이지 번호", example = "0")
    private int page;
}
