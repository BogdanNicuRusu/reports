package com.nicu.reports.csv;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CsvPopulator {

    private static final String ROW_DELIMITER = "\n";

    private static final String CELL_DELIMITER = ",";

    private final int minimumNrRows;

    private final int maximumNrRows;

    private final List<String> columnHeaders;

    public CsvPopulator(@Value("${csv.rows.minimum:5}") int minimumNrRows,
                        @Value("${csv.rows.maximum:10}") int maximumNrRows,
                        @Value("#{'${csv.column.headers}'.split(',')}") List<String> columnHeaders) {
        this.minimumNrRows = minimumNrRows;
        this.maximumNrRows = maximumNrRows;
        this.columnHeaders = columnHeaders;
    }

    String computeCsvContent() {
        int nrRows = (int) (Math.random() * ((maximumNrRows - minimumNrRows) + 1)) + minimumNrRows;
        StringBuffer buffer = new StringBuffer();
        buffer.append(computeCsvHeader()).append(ROW_DELIMITER);
        IntStream.rangeClosed(1, nrRows)
            .forEach(index -> buffer.append(computeCsvRow()).append(ROW_DELIMITER));
        return buffer.toString();
    }

    private String computeCsvHeader() {
        return String.join(CELL_DELIMITER, columnHeaders);
    }

    private String computeCsvRow() {
        return IntStream.rangeClosed(1, columnHeaders.size())
            .mapToObj(index -> UUID.randomUUID().toString())
            .collect(Collectors.joining(CELL_DELIMITER));
    }
}
