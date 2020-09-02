package com.nicu.reports.csv;

import static com.nicu.reports.csv.CsvCreator.CSV_EXTENSION;
import static com.nicu.reports.service.ReportService.REPORT_NOT_FOUND;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

import com.nicu.reports.exception.NotFoundEntityException;
import com.nicu.reports.model.Report;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class CsvRetriever {

    private final CsvCreator csvCreator;

    public String retrieveReportContent(Report report) {
        String dirPath = csvCreator.computeDirectoryPath(report);
        String filename = computeFullPath(dirPath, report);
        Path path = Paths.get(filename);
        try {
            return String.join("\n", Files.readAllLines(path));
        } catch (IOException e) {
            log.error("Could not read file from {}", filename);
            throw new NotFoundEntityException(REPORT_NOT_FOUND);
        }
    }

    private String computeFullPath(String dirPath, Report report) {
        return dirPath + File.separator + report.getId() + CSV_EXTENSION;
    }
}
