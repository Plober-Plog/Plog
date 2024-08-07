package com.plog.backend.domain.report.service;

import com.plog.backend.domain.report.dto.request.ReportCreateRequestDto;
import com.plog.backend.domain.report.dto.response.ReportResultResponseDto;

public interface ReportService {
    ReportResultResponseDto createReport(Long plantDiaryId);
}
