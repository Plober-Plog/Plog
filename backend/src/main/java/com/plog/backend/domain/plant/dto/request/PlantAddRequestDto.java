package com.plog.backend.domain.plant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantAddRequestDto {
    @Schema(description = "기본 식물 종류 ID (2부터 유효한 값)", example = "2")
    private Long plantTypeId;

    @Schema(description = "기타 식물 종류 ID (2부터 유효한 값)", example = "1")
    private Long otherPlantTypeId;

    @Schema(description = "프로필 사진")
    private MultipartFile profile;

    @Schema(description = "식물 소개", example = "이 식물은 밝은 간접광에서 잘 자랍니다.")
    private String bio;

    @Schema(description = "식물 별명", example = "초록이")
    private String nickname;

    @Schema(description = "식물 생일", example = "2023-01-01")
    private LocalDate birthDate;

}