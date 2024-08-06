package com.plog.backend.domain.sns.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ArticleGetSimpleResponseDto {
    String nickname;
    String image;
    String content;
    int likeCnt;
    int commentCnt;
    boolean isBookmarked;
}
