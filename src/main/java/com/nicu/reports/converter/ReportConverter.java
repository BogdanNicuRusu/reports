package com.nicu.reports.converter;

import static com.nicu.reports.model.Report.ReportStatus.PENDING;
import static com.nicu.reports.model.Report.ReportType.valueOf;
import static java.time.LocalDateTime.now;

import com.nicu.reports.api.ReportInfo;
import com.nicu.reports.dto.ReportDTO;
import com.nicu.reports.model.Report;

public class ReportConverter {

    public static Report toReport(ReportInfo reportInfo, Long userId) {
        return new Report(reportInfo.getName(), valueOf(reportInfo.getType()), PENDING, now(), now(), userId);
    }

    public static ReportDTO toReportDTO(Report report) {
        return new ReportDTO(report.getId(), report.getName(), report.getType(), report.getStatus(), report.getCreated(), report.getUpdated());
    }
}
