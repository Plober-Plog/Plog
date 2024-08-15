package com.plog.backend.domain.sns.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    String searchId;
    String content;
    Long parentId;
    LocalDateTime createDate;
    LocalDateTime updateDate;
    int state;
}
