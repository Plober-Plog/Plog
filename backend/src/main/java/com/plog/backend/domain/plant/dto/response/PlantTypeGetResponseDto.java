package com.plog.backend.domain.plant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantTypeGetResponseDto {

    @Schema(description = "식물 이름", example = "몬스테라")
    private String plantName;

//    @Schema(description = "식물 프로필 URL", example = "http://example.com/profile.jpg")
//    private String profile;

    @Schema(description = "식물 관리 가이드", example = "이 식물은 밝은 간접광에서 잘 자랍니다.")
    private String guide;

    @Schema(description = "물 주기 (일 단위)", example = "7")
    private int waterInterval;

    @Schema(description = "비료 주기 (일 단위)", example = "30")
    private int fertilizeInterval;

    @Schema(description = "분갈이 주기 (일 단위)", example = "365")
    private int repotInterval;
}
