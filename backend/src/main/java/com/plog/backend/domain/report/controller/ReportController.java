package com.plog.backend.domain.report.controller;

import com.plog.backend.domain.report.dto.response.ReportResultResponseDto;
import com.plog.backend.domain.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/report")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Report API", description = "Report 생성 API")
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    @Operation(summary = "Create Report", description = "식물 일지 ID를 기반으로 보고서를 생성합니다.")
    public ResponseEntity<?> createReport(
            @Parameter(description = "Plant Diary ID", required = true) @RequestParam Long plantDiaryId) {
        log.info("Received request to create report for plantDiaryId: {}", plantDiaryId);
        ReportResultResponseDto responseDto = reportService.createReport(plantDiaryId);
        log.info("Sending response: {}", responseDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
