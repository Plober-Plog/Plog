package com.plog.backend.domain.sns.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ArticleAddRequestDto {
    private String content;

    private MultipartFile[] images;

    private List<Long> tagTypeList;

    private int visibility;
}
