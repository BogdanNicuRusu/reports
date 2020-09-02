package com.nicu.reports.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nicu.reports.model.Report;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CsvCreator {

    public static final String CSV_EXTENSION = ".csv";

    private final String baseDir;

    private final CsvPopulator csvPopulator;

    public CsvCreator(@Value("${csv.base_dir:reports}") String baseDir,
                      CsvPopulator csvPopulator) {
        this.baseDir = baseDir;
        this.csvPopulator = csvPopulator;
    }

    public boolean createCsvFile(Report report) {
        String content = csvPopulator.computeCsvContent();
        String directoryPath = computeDirectoryPath(report);
        boolean existingDirectory = createDirectoryIfNotExists(directoryPath);
        return existingDirectory && writeToFile(directoryPath, report.getId(), content);
    }

    String computeDirectoryPath(Report report) {
        return baseDir + File.separator + report.getUserId();
    }

    private boolean createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.exists() || directory.isDirectory()) {
            return true;
        }
        try {
            return directory.mkdirs();
        } catch (SecurityException e) {
            log.error("Cannot create folder {}: {}", directoryPath, e.getMessage());
            return false;
        }
    }

    private boolean writeToFile(String directoryPath, long filename, String content) {
        Path path = Paths.get(directoryPath + File.separator + filename + CSV_EXTENSION);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(content);
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
