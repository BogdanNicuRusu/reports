package com.nicu.reports.dao.impl;

import java.sql.ResultSet;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.nicu.reports.dao.UserDao;
import com.nicu.reports.model.User;

@Component
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findById(long id) {
        return jdbcTemplate.queryForObject("SELECT ID, USERNAME, PASSWORD, TOKEN FROM USERS WHERE ID = ?", new Object[]{ id },
            (ResultSet resultSet, int rowNumber) ->
                new User(resultSet.getLong("ID"), resultSet.getString("USERNAME"), resultSet.getString("PASSWORD"), resultSet.getString("TOKEN")));
    }

    @Override
    public User findByUsername(String username) {
        return jdbcTemplate.queryForObject("SELECT ID, USERNAME, PASSWORD, TOKEN FROM USERS WHERE USERNAME = ?", new Object[]{ username },
            (ResultSet resultSet, int rowNumber) ->
                new User(resultSet.getLong("ID"), resultSet.getString("USERNAME"), resultSet.getString("PASSWORD"), resultSet.getString("TOKEN")));
    }

    @Override
    public int countByUsername(String username) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM USERS WHERE USERNAME = ?", new Object[]{ username }, Integer.class);
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update("INSERT INTO USERS(USERNAME, PASSWORD, TOKEN) VALUES (?, ?, ?)", user.getUsername(), user.getPassword(), user.getToken());
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update("UPDATE USERS SET PASSWORD = ?, TOKEN = ? WHERE USERNAME = ?", user.getPassword(), user.getToken(), user.getUsername());
    }
}
