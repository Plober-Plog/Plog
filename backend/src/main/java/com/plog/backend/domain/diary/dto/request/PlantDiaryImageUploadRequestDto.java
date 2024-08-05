package com.plog.backend.domain.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlantDiaryImageUploadRequestDto {
    @Schema(description = "일지에 업로드 할 사진")
    private MultipartFile image;

    @Schema(description = "대표 사진 여부", example = "true")
    private boolean isThumbnail;
}
