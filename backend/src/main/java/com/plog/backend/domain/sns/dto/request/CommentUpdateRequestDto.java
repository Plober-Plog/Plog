package com.plog.backend.domain.sns.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CommentUpdateRequestDto {
    private Long commentId;
    private String comment;
}
