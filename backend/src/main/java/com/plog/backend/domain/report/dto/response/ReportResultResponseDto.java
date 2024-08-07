package com.plog.backend.domain.report.dto.response;

import com.plog.backend.domain.report.entity.ReportResult;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class ReportResultResponseDto {
    private String plantName;
    private String firstDayImageUrl;
    private String recentImageUrl;
    private int waterResult;
    private int fertilizeResult;
    private int repoResult;
    private int waterData;
    private int fertilizeData;
    private int repotData;
}
