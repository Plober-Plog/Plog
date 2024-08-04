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
    private Long plantId;

    @Schema(description = "기본 식물 종류 ID (2부터 유효한 값)", example = "1")
    private Long plantTypeId;

    @Schema(description = "기타 식물 종류 ID (2부터 유효한 값)", example = "2")
    private Long otherPlantId;

    @Schema(description = "식물 별명", example = "초록이")
    private String nickname;

    @Schema(description = "식물에 대한 설명", example = "무럭 무럭 잘 자라라")
    private String bio;

//    @Schema(description = "식물 프로필 URL", example = "http://example.com/profile.jpg")
//    private String profile;

    @Schema(description = "식물 생일", example = "2023-01-01")
    private LocalDate birthDate;

    @Schema(description = "식물 이별일", example = "2024-01-01")
    private LocalDate deadDate;

    @Schema(description = "알림 여부", example = "true")
    private boolean hasNotified;

    @Schema(description = "고정 여부", example = "true")
    boolean isFixed;

    @Schema(description = "정렬 순서", example = "1")
    int order;

    @Schema(description = "삭제 여부", example = "false")
    boolean isDeleted;
}
