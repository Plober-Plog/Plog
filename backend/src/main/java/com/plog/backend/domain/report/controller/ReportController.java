package com.plog.backend.domain.report.controller;

import com.plog.backend.domain.report.dto.request.ReportCreateRequestDto;
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

    @GetMapping("/{plantId}")
    @Operation(summary = "Create Report", description = "식물 일지 ID를 기반으로 보고서를 생성합니다.")
    public ResponseEntity<?> createReport(
            @PathVariable("plantId") Long plantId) {
        log.info(">>> [POST] 분석 리포트 생성 plantId: {}", plantId);
        ReportResultResponseDto responseDto = reportService.createReport(plantId);
        log.info(">>> [POST] 분석 리포트 전송 : {}", responseDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
