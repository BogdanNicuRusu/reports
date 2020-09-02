package com.nicu.reports.scheduler;

import static com.nicu.reports.model.Report.ReportStatus.DONE;
import static com.nicu.reports.model.Report.ReportStatus.FAILED;
import static com.nicu.reports.model.Report.ReportStatus.PENDING;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nicu.reports.dao.ReportDao;
import com.nicu.reports.model.Report;
import com.nicu.reports.service.ReportService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class ReportProcessorJob extends AbstractQuartzJob<Report> {

    public static final String REPORT_PROCESSOR_JOB_NAME = "Report processor job";

    public static final String REPORT_PROCESSOR_JOB_DESCRIPTION = "Job processing pending reports, generating csv files and marking the reports as DONE or FAILED";

    public static final String REPORT_PROCESSOR_JOB_TRIGGER = "Report processor job trigger";

    private final ReportDao reportDao;

    private final ReportService reportService;

    @Override
    List<Report> readItems() {
        return reportDao.findByStatus(PENDING);
    }

    @Override
    void processItem(Report report) {
        reportService.processReport(report);
    }

    @Override
    long successes(List<Report> reports) {
        return reports.stream()
            .filter(report -> report.getStatus() == DONE)
            .count();
    }

    @Override
    long failures(List<Report> reports) {
        return reports.stream()
            .filter(report -> report.getStatus() == FAILED)
            .count();
    }
}
