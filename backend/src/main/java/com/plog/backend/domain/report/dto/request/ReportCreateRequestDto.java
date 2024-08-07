package com.plog.backend.domain.report.dto.request;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ReportCreateRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
}
