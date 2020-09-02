package com.nicu.reports.service;

import static com.nicu.reports.converter.ReportConverter.toReport;
import static com.nicu.reports.converter.ReportConverter.toReportDTO;
import static com.nicu.reports.model.Report.ReportStatus.DONE;
import static com.nicu.reports.model.Report.ReportStatus.FAILED;
import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.nicu.reports.api.ReportInfo;
import com.nicu.reports.converter.ReportConverter;
import com.nicu.reports.csv.CsvCreator;
import com.nicu.reports.csv.CsvRetriever;
import com.nicu.reports.dao.impl.ReportDaoImpl;
import com.nicu.reports.dto.ReportDTO;
import com.nicu.reports.exception.DuplicatedNameException;
import com.nicu.reports.exception.NotFoundEntityException;
import com.nicu.reports.model.Report;
import com.nicu.reports.model.Report.ReportStatus;
import com.nicu.reports.model.Report.ReportType;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ReportService {

    public static final String REPORT_NOT_FOUND = "Report not found";

    private static final String EXISTING_REPORT_NAME = "Already existing report name";

    private final ReportDaoImpl reportDao;

    private final CsvCreator csvCreator;

    private final CsvRetriever csvRetriever;

    public ReportDTO findByReportIdAndUserId(long reportId, long userId) {
        return toReportDTO(findById(reportId, userId));
    }

    public String findReportContent(long reportId, long userId) {
        Report report = findById(reportId, userId);
        return csvRetriever.retrieveReportContent(report);
    }

    public List<ReportDTO> getUserReports(long userId) {
        return reportDao.findByUserId(userId).stream()
            .map(ReportConverter::toReportDTO)
            .collect(toList());
    }

    public ReportDTO saveReport(ReportInfo reportInfo, Long userId) {
        if (reportDao.countByName(reportInfo.getName()) > 0) {
            log.error("Report with name {} is already existing", reportInfo.getName());
            throw new DuplicatedNameException(EXISTING_REPORT_NAME);
        }
        Report report = toReport(reportInfo, userId);
        reportDao.save(report);
        return toReportDTO(reportDao.findByNameAndUserId(reportInfo.getName(), userId));
    }

    public void processReport(Report report) {
        boolean success = csvCreator.createCsvFile(report);
        updateReportStatus(report, success ? DONE : FAILED);
        reportDao.update(report);
    }

    public void deleteReport(long reportId, long userId) {
        Report report = findById(reportId, userId);
        reportDao.delete(report);
    }

    public ReportDTO editReport(long reportId, ReportInfo reportInfo, long userId) {
        Report report = findById(reportId, userId);
        if (!report.getName().equals(reportInfo.getName()) && reportDao.countByName(reportInfo.getName()) > 0) {
            log.error("Report with name {} is already existing", reportInfo.getName());
            throw new DuplicatedNameException(EXISTING_REPORT_NAME);
        }
        report.setName(reportInfo.getName());
        report.setType(ReportType.valueOf(reportInfo.getType()));
        report.setUpdated(now());
        reportDao.update(report);
        return toReportDTO(report);
    }

    private Report findById(long reportId, long userId) {
        try {
            return reportDao.findByReportIdAndUserId(reportId, userId);
        } catch (EmptyResultDataAccessException e) {
            log.error("Report with id {} for user with {} can not be found", reportId, userId);
            throw new NotFoundEntityException(REPORT_NOT_FOUND);
        }
    }

    private void updateReportStatus(Report report, ReportStatus reportStatus) {
        report.setStatus(reportStatus);
        report.setUpdated(now());
    }
}
