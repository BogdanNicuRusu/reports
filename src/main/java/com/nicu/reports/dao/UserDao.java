package com.nicu.reports.dao;

import com.nicu.reports.model.User;

public interface UserDao {

    User findById(long id);

    User findByUsername(String username);

    int countByUsername(String username);

    void save(User user);

    void update(User user);
}
