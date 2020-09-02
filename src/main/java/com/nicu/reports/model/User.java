package com.nicu.reports.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private Long id;

    private String username;

    private String password;

    private String token;

    public User(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
    }

    public User(Long id, String username, String password, String token) {
        this(username, password, token);
        this.id = id;
    }

}
