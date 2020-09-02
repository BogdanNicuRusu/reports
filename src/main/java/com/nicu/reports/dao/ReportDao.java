package com.nicu.reports.dao;

import java.util.List;

import com.nicu.reports.model.Report;
import com.nicu.reports.model.Report.ReportStatus;

public interface ReportDao {

    Report findByReportIdAndUserId(long id, long userId);

    List<Report> findByUserId(long userId);

    Report findByNameAndUserId(String name, long userId);

    List<Report> findByStatus(ReportStatus status);

    int countByName(String name);

    void save(Report report);

    void update(Report report);

    void delete(Report report);
}
