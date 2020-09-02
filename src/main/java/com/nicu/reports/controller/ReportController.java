package com.nicu.reports.controller;

import static com.nicu.reports.csv.CsvCreator.CSV_EXTENSION;
import static com.nicu.reports.service.ReportService.REPORT_NOT_FOUND;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicu.reports.api.ReportInfo;
import com.nicu.reports.dto.ReportDTO;
import com.nicu.reports.exception.NotFoundEntityException;
import com.nicu.reports.service.ReportService;
import com.nicu.reports.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/reports")
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;

    private final TokenService tokenService;

    @Operation(summary = "Get reports belonging to a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of reports belonging to user",
            content = { @Content(mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ReportDTO.class)) }),
        @ApiResponse(responseCode = "401", description = "Invalid authorization",
            content = @Content) })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReportDTO>> getUserReports(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = tokenService.getUserId(authorizationHeader);
        return ResponseEntity.ok(reportService.getUserReports(userId));
    }

    @Operation(summary = "Create new report")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Report created",
            content = { @Content(mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ReportDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid report data"),
        @ApiResponse(responseCode = "401", description = "Invalid authorization") })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportDTO> saveReport(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody ReportInfo reportInfo) {
        Long userId = tokenService.getUserId(authorizationHeader);
        return new ResponseEntity<>(reportService.saveReport(reportInfo, userId), CREATED);
    }

    @Operation(summary = "Get report by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report found",
            content = { @Content(mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ReportDTO.class)) }),
        @ApiResponse(responseCode = "401", description = "Invalid authorization"),
        @ApiResponse(responseCode = "404", description = "Report not found") })
    @GetMapping(value = "/{reportId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportDTO> getReport(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("reportId") long reportId) {
        Long userId = tokenService.getUserId(authorizationHeader);
        return ResponseEntity.ok(reportService.findByReportIdAndUserId(reportId, userId));
    }

    @Operation(summary = "Download report")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report found",
            content = { @Content(mediaType = "text/csv") }),
        @ApiResponse(responseCode = "401", description = "Invalid authorization"),
        @ApiResponse(responseCode = "404", description = "Report not found") })
    @GetMapping(value = "/{reportId}", produces = "text/csv")
    public void getReport(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("reportId") long reportId, HttpServletResponse response) {
        Long userId = tokenService.getUserId(authorizationHeader);
        response.setContentType("text/csv");
        try {
            response.getWriter().println(reportService.findReportContent(reportId, userId));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new NotFoundEntityException(REPORT_NOT_FOUND);
        }
    }

    @Operation(summary = "Delete report")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Report deleted"),
        @ApiResponse(responseCode = "401", description = "Invalid authorization"),
        @ApiResponse(responseCode = "404", description = "Report not found") })
    @DeleteMapping("/{reportId}")
    public ResponseEntity<ReportDTO> deleteReport(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("reportId") long reportId) {
        Long userId = tokenService.getUserId(authorizationHeader);
        reportService.deleteReport(reportId, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update report")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report updated",
            content = { @Content(mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ReportDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid report data"),
        @ApiResponse(responseCode = "401", description = "Invalid authorization"),
        @ApiResponse(responseCode = "404", description = "Report not found") })
    @PutMapping(value = "/{reportId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportDTO> editReport(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("reportId") long reportId,
                                                @Valid @RequestBody ReportInfo reportInfo) {
        Long userId = tokenService.getUserId(authorizationHeader);
        return ResponseEntity.ok(reportService.editReport(reportId, reportInfo, userId));
    }
}
