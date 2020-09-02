package com.nicu.reports.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Report {

    private Long id;

    private String name;

    private ReportType type;

    private ReportStatus status;

    private LocalDateTime created;

    private LocalDateTime updated;

    private Long userId;

    public Report(String name, ReportType type, ReportStatus status, LocalDateTime created, LocalDateTime updated, Long userId) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.created = created;
        this.updated = updated;
        this.userId = userId;
    }

    public Report(Long id, String name, ReportType type, ReportStatus status, LocalDateTime created, LocalDateTime updated, Long userId) {
        this(name, type, status, created, updated, userId);
        this.id = id;
    }

    public enum ReportType {
        A, B, C
    }

    public enum ReportStatus {
        PENDING, DONE, FAILED
    }
}
