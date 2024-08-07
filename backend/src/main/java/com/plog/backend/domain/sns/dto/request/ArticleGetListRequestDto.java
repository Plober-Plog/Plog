package com.plog.backend.domain.sns.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ArticleGetListRequestDto {
    Long userId;
    String searchId;
    int page;
    List<Integer> tagType;
    String keyword;
}
