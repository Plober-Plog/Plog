package com.plog.backend.domain.report.entity;

import com.plog.backend.global.exception.NotValidRequestException;
import lombok.*;

public enum ReportResult {
    GREAT(1),
    GOOD(2),
    BAD(3),
    WORST(4);

    private int value;

    ReportResult(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ReportResult reportResult(int value) {
        for (ReportResult reportResult : ReportResult.values()) {
            if (reportResult.getValue() == value) {
                return reportResult;
            }
        }
        throw new NotValidRequestException("ReportResult - Invalid value: " + value);
    }

    public static ReportResult getReportResult(int rating) {
        if (rating >= 0 && rating < 5) {
            return ReportResult.GREAT;
        } else if (rating >= 5 && rating < 10) {
            return ReportResult.GOOD;
        } else if (rating >= 10 && rating < 15) {
            return ReportResult.BAD;
        } else if (rating >= 15) {
            return ReportResult.WORST;
        } else {
            throw new NotValidRequestException("Invalid rating value: " + rating);
        }
    }
}
