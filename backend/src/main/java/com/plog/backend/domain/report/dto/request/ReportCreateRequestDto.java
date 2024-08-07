package com.plog.backend.domain.report.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ReportCreateRequestDto {
    private Long plantDiaryId;
}
