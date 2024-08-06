package com.plog.backend.domain.sns.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Schema(description = "게시글 추가 요청 DTO")
public class ArticleAddRequestDto {

    @Schema(description = "게시글 내용", example = "오늘은 날씨가 좋네요.")
    private String content;

    @Schema(description = "첨부 이미지 파일 목록")
    private MultipartFile[] images;

    @Schema(description = "태그 타입 목록", example = "[1, 2, 3]")
    private List<Long> tagTypeList;

    @Schema(description = "게시글 공개 범위 (1 ~ 4)", example = "1")
    private int visibility;
}
