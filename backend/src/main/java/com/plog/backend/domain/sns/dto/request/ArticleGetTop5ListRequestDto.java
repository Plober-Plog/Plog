package com.plog.backend.domain.sns.dto.request;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ArticleGetTop5ListRequestDto {
    Long userId;
    List<Integer> tagType;
    int orderType;
}
