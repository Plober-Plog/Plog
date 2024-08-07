package com.plog.backend.domain.sns.dto.response;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ArticleCommentGetResponse {
    Long articleCommentId;
    Long userId;
    String profile;
    String nickname;
    String content;
    Long parentId;
    LocalDate createDate;
    LocalDate updateDate;
    int state;
}
