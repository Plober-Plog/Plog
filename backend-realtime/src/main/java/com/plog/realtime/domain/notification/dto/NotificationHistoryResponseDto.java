package com.plog.realtime.domain.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class NotificationHistoryResponseDto {
    @Schema(description = "알림을 식별할 id 값", example = "1")
    private Long notificationId;
    @Schema(description = "알림 타입", example = "COMMENT")
    private String type;
    @Schema(description = "알림 읽었는지 여부", example = "false")
    private boolean isRead;
    @Schema(description = "알림 상세 내용", example = "testId1님이 testId2의 게시글에 댓글을 달았어요!")
    private String content;
    @Schema(description = "클릭시 가야하는 url", example = "https://i11b308.p.ssafy.io/plant/1")
    private String clickUrl;
    @Schema(description = "알림 시각", example = "2024-01-01")
    private LocalDate notificationDate;
}
