package com.nicu.reports.dto;

import java.time.LocalDateTime;

import com.nicu.reports.model.Report.ReportStatus;
import com.nicu.reports.model.Report.ReportType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportDTO {

    private long id;

    private String name;

    private ReportType type;

    private ReportStatus status;

    private LocalDateTime created;

    private LocalDateTime updated;
}
