package com.plog.backend.domain.sns.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ArticleBookmarkGetResponseDto {
    private List<BookmarkDto> bookmarks;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString
    public static class BookmarkDto {
        private Long id;
        private Long articleId;
        private String articleTitle;
        private String articleContent;
        private String createdBy;
        private String createdAt;
    }
}