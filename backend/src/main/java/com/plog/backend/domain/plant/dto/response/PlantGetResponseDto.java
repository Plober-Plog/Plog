package com.plog.backend.domain.plant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantGetResponseDto {
    @Schema(description = "식물 ID", example = "1")
    Long plantId;

    @Schema(description = "기본 식물 종류 ID (2부터 유효한 값)", example = "1")
    Long plantTypeId;

    @Schema(description = "기타 식물 종류 ID (2부터 유효한 값)", example = "2")
    Long otherPlantId;

    @Schema(description = "식물 별명", example = "초록이")
    String nickname;

//    @Schema(description = "식물 프로필 URL", example = "http://example.com/profile.jpg")
//    String profile;

    @Schema(description = "식물 생일", example = "2023-01-01")
    LocalDate birthDate;

    @Schema(description = "식물 이별일", example = "2024-01-01")
    LocalDate deathDate;

    @Schema(description = "알림 여부", example = "true")
    boolean hasNotified;

    @Schema(description = "고정 순서 - 추후 변경 예정", example = "1")
    int fixed;

    @Schema(description = "삭제 여부", example = "false")
    boolean isDeleted;
}
