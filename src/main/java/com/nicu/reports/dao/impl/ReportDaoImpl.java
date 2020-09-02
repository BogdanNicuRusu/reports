package com.nicu.reports.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.nicu.reports.dao.ReportDao;
import com.nicu.reports.model.Report;
import com.nicu.reports.model.Report.ReportStatus;
import com.nicu.reports.model.Report.ReportType;

@Component
public class ReportDaoImpl implements ReportDao {

    private final JdbcTemplate jdbcTemplate;

    public ReportDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Report findByReportIdAndUserId(long reportId, long userId) {
        return jdbcTemplate.queryForObject("SELECT ID, NAME, TYPE, STATUS, CREATED, UPDATED, USER_ID FROM REPORTS WHERE ID = ? AND USER_ID = ?", new Object[]{ reportId, userId },
            this::reportRowMapper);
    }

    @Override
    public List<Report> findByUserId(long userId) {
        return jdbcTemplate.query("SELECT ID, NAME, TYPE, STATUS, CREATED, UPDATED, USER_ID FROM REPORTS WHERE USER_ID = ?", new Object[]{ userId }, this::reportRowMapper);
    }

    @Override
    public Report findByNameAndUserId(String name, long userId) {
        return jdbcTemplate.queryForObject("SELECT ID, NAME, TYPE, STATUS, CREATED, UPDATED, USER_ID FROM REPORTS WHERE NAME = ? AND USER_ID = ?", new Object[]{ name, userId },
            this::reportRowMapper);
    }

    @Override
    public List<Report> findByStatus(ReportStatus status) {
        return jdbcTemplate.query("SELECT ID, NAME, TYPE, STATUS, CREATED, UPDATED, USER_ID FROM REPORTS WHERE STATUS = ?", new Object[]{ status.name() }, this::reportRowMapper);
    }

    @Override
    public int countByName(String name) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM REPORTS WHERE NAME = ?", new Object[]{ name }, Integer.class);
    }

    @Override
    public void save(Report report) {
        jdbcTemplate.update("INSERT INTO REPORTS(NAME, TYPE, STATUS, CREATED, UPDATED, USER_ID) VALUES (?, ?, ?, ?, ?, ?)", report.getName(), report.getType().name(),
            report.getStatus().name(),
            report.getCreated(), report.getUpdated(), report.getUserId());
    }

    @Override
    public void update(Report report) {
        jdbcTemplate.update("UPDATE REPORTS SET NAME = ?, TYPE = ?, STATUS = ?, UPDATED = ? WHERE ID = ?", report.getName(), report.getType().toString(), report.getStatus().name(),
            report.getUpdated(), report.getId());
    }

    private Report reportRowMapper(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Report(resultSet.getLong("ID"), resultSet.getString("NAME"), ReportType.valueOf(resultSet.getString("TYPE")),
            ReportStatus.valueOf(resultSet.getString("STATUS")),
            resultSet.getTimestamp("CREATED").toLocalDateTime(), resultSet.getTimestamp("UPDATED").toLocalDateTime(), resultSet.getLong("USER_ID"));
    }

    @Override
    public void delete(Report report) {
        jdbcTemplate.update("DELETE FROM REPORTS WHERE ID = ?", report.getId());
    }
}
