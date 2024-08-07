package com.plog.backend.domain.report.controller;

import com.plog.backend.domain.report.dto.request.ReportAddRequestDto;
import com.plog.backend.domain.report.entity.Report;
import com.plog.backend.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class ReportController {
    private final ReportService reportService;

    public ResponseEntity<?> addReport(@RequestBody ReportAddRequestDto) {
        return null;
    }
}
