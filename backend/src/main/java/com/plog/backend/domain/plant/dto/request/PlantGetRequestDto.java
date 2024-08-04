package com.plog.backend.domain.plant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantGetRequestDto {
    @Schema(description = "회원을 식별할 id 값", example = "testid")
    private String searchId;

    @Schema(description = "기본 식물 종류 ID (2부터 유효한 값)", example = "2")
    private Long plantTypeId;

    @Schema(description = "기타 식물 종류 ID (2부터 유효한 값)", example = "1")
    private Long otherPlantTypeId;

    @Schema(description = "목록 조회 슬라이싱을 위한 페이지 번호", example = "0")
    private int page;
}
